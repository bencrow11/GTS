package org.pokesplash.gts.util;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public abstract class Utils {
	/**
	 * Method to write some data to file.
	 * @param filePath the directory to write the file to
	 * @param filename the name of the file
	 * @param data the data to write to file
	 * @return CompletableFuture if writing to file was successful
	 */
	public static CompletableFuture<Boolean> writeFileAsync(String filePath, String filename, String data) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		Path path = Paths.get(new File("").getAbsolutePath() + filePath, filename);
		File file = path.toFile();

		// If the path doesn't exist, create it.
		if (!Files.exists(path.getParent())) {
			file.getParentFile().mkdirs();
		}

		// Write the data to file.
		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
				path,
				StandardOpenOption.WRITE,
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING
		)) {
			ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));

			fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					attachment.clear();
					try {
						fileChannel.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					future.complete(true);
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					future.complete(writeFileSync(file, data));
				}
			});
		} catch (IOException | SecurityException e) {
			Gts.LOGGER.fatal("Unable to write file asynchronously, attempting sync write.");
			future.complete(future.complete(false));
		}

		return future;
	}

	/**
	 * Method to write a file sync.
	 * @param file the location to write.
	 * @param data the data to write.
	 * @return true if the write was successful.
	 */
	public static boolean writeFileSync(File file, String data) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
			return true;
		} catch (Exception e) {
			Gts.LOGGER.fatal("Unable to write to file for " + Gts.MOD_ID + ".\nStack Trace: ");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteFile(String filePath, String filename) {
		try {
			Path path = Paths.get(new File("").getAbsolutePath() + filePath, filename);
			File file = path.toFile();

			if (!file.exists()) {
				return true;
			}

			file.delete();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Method to read a file asynchronously
	 * @param filePath the path of the directory to find the file at
	 * @param filename the name of the file
	 * @param callback a callback to deal with the data read
	 * @return true if the file was read successfully
	 */
	public static CompletableFuture<Boolean> readFileAsync(String filePath, String filename,
	                                                       Consumer<String> callback) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Path path = Paths.get(new File("").getAbsolutePath() + filePath, filename);
		File file = path.toFile();

		if (!file.exists()) {
			future.complete(false);
			executor.shutdown();
			return future;
		}

		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
			ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size()); // Allocate buffer for the entire file

			Future<Integer> readResult = fileChannel.read(buffer, 0);
			readResult.get(); // Wait for the read operation to complete
			buffer.flip();

			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			String fileContent = new String(bytes, StandardCharsets.UTF_8);

			callback.accept(fileContent);

			fileChannel.close();
			executor.shutdown();
			future.complete(true);
		} catch (Exception e) {
			future.complete(readFileSync(file, callback));
			executor.shutdown();
		}

		return future;
	}

	/**
	 * Method to read files sync.
	 * @param file The file to read
	 * @param callback what to do with the read data.
	 * @return true if the file could be read successfully.
	 */
	public static boolean readFileSync(File file, Consumer<String> callback) {
		try {
			Scanner reader = new Scanner(file);

			String data = "";

			while (reader.hasNextLine()) {
				data += reader.nextLine();
			}
			reader.close();
			callback.accept(data);
			return true;
		} catch (Exception e) {
			Gts.LOGGER.fatal("Unable to read file " + file.getName() + " for " + Gts.MOD_ID + ".\nStack Trace: ");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to check if a directory exists. If it doesn't, create it.
	 * @param path The directory to check.
	 * @return the directory as a File.
	 */
	public static File checkForDirectory(String path) {
		File dir = new File(new File("").getAbsolutePath() + path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * Method to create a new gson builder.
	 * @return Gson instance.
	 */
	public static Gson newGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * Formats a message by removing minecraft formatting codes if sending to console.
	 * @param message The message to format.
	 * @param isPlayer If the sender is a player or console.
	 * @return String that is the formatted message.
	 */
	public static String formatMessage(String message, Boolean isPlayer) {
		if (isPlayer) {
			return message.trim();
		} else {
			return message.replaceAll("§[0-9a-fk-or]", "").trim();
		}
	}

	/**
	 * Checks if a string can be parsed to integer.
	 * @param string the string to try and parse.
	 * @return true if the string can be parsed.
	 */
	public static boolean isStringInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String parseLongDate(long time) {
		// 1000 ms in 1 s
		// 60s in 1 m
		// 60m in 1 h
		// 24h in 1 d
		long second = 1000;
		long minute = second * 60;
		long hour = minute * 60;
		long day = hour * 24;

		long timeLeft = time;
		String output = "";

		if (timeLeft > day) {
			output += (time - (time % day)) / day + "d ";
			timeLeft = timeLeft % day;
		}

		if (timeLeft > hour) {
			output += (timeLeft - (timeLeft % hour)) / hour + "h ";
			timeLeft = timeLeft % hour;
		}

		if (timeLeft > minute) {
			output += (timeLeft - (timeLeft % minute)) / minute + "m ";
			timeLeft = timeLeft % minute;
		}

		if (timeLeft > second) {
			output += (timeLeft - (timeLeft % second)) / second + "s ";
			timeLeft = timeLeft % second;
		}

		return output;
	}

	public static String capitaliseFirst(String message) {

		if (message.contains("[") || message.contains("]")) {
			return message.replaceAll("\\[|\\]", "");
		}

		if (message.contains("_")) {
			String[] messages = message.split("_");
			String output = "";
			for (String msg : messages) {
				output += capitaliseFirst(msg);
			}
			return output;
		}

		return message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
	}

	public static boolean isHA(Pokemon pokemon) {
		if (pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).size() != 1) {
			return false;
		}
		String ability =
				pokemon.getForm().getAbilities().getMapping().get(Priority.LOW).get(0).getTemplate().getName();

		return pokemon.getAbility().getName().equalsIgnoreCase(ability);
	}

	public static String formatPlaceholders(String message, double minPrice, String listing,
	                                        String seller, String buyer) {
		String newMessage = message;
		if (message == null) {
			return "";
		}

		if (listing != null) {
			newMessage = newMessage.replaceAll("\\{listing\\}", listing);
		}

		if (seller != null) {
			newMessage = newMessage.replaceAll("\\{seller\\}", seller);
		}

		if (buyer != null) {
			newMessage = newMessage.replaceAll("\\{buyer\\}", buyer);
		}

		return newMessage
				.replaceAll("\\{min_price\\}", "" + minPrice)
				.replaceAll("\\{max_listings\\}", "" + Gts.config.getMax_listings_per_player())
				.replaceAll("\\{max_price\\}", "" + Gts.config.getMaximum_price());
	}

	public static ItemStack parseItemId(String id) {
		CompoundTag tag = new CompoundTag();
		tag.putString("id", id);
		tag.putInt("Count", 1);
		return ItemStack.of(tag);
	}

	public static void removeAllTimers() {
		for (PokemonListing listing : Gts.timers.getPokemonTimers()) {
			Gts.timers.deleteTimer(listing);
		}
		for (ItemListing listing : Gts.timers.getItemTimers()) {
			Gts.timers.deleteTimer(listing);
		}
	}

	public static void broadcastClickable(String message, String command) {
		Component component = Component.literal(message).setStyle(Style.EMPTY.withClickEvent(
				new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));

		MinecraftServer server = Gts.server;
		ArrayList<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());

		for (ServerPlayer pl : players) {
			pl.sendSystemMessage(component);
		}
	}
}
