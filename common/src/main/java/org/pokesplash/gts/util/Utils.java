package org.pokesplash.gts.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.NBTComponent;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;
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
					Gts.LOGGER.fatal("Unable to write to file for " + Gts.MOD_ID + ".\nStack Trace: " + exc.getStackTrace());
					future.complete(false);
				}
			});
		} catch (IOException | SecurityException e) {
			Gts.LOGGER.fatal("Unable to write to file for " + Gts.MOD_ID + ".\nStack Trace: " + e.getStackTrace());
			future.complete(false);
		}

		return future;
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
			Gts.LOGGER.fatal("Unable to read file " + filename + " for " + Gts.MOD_ID + ".\nStack Trace: " + e.getStackTrace());
			executor.shutdown();
			future.completeExceptionally(e);
		}

		return future;
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
}
