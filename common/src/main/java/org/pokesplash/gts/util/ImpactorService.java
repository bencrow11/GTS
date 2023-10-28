package org.pokesplash.gts.util;

import net.impactdev.impactor.api.economy.EconomyService;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.currency.Currency;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class to interact with the Impactor API
 */
public abstract class ImpactorService {

	// The impactor service
	private static EconomyService service = EconomyService.instance();

	// The currency used for this mod.
	private static Currency currency = service.currencies().primary();

	/**
	 * Method to get the account of a player.
	 * @param uuid The UUID of the player.
	 * @return The account of the player.
	 */

	public static Account getAccount(UUID uuid) {
		if (!service.hasAccount(uuid).join()) {
			return service.account(uuid).join();
		}
		return service.account(currency, uuid).join();
	}

	/**
	 * Method to add to the balance of an account.
	 * @param account The account to add the balance to.
	 * @param amount The amount to add.
	 * @return true if the transaction was successful.
	 */
	public static boolean add(Account account, double amount) {
		EconomyTransaction transaction = account.depositAsync(new BigDecimal(amount)).join();

		return transaction.successful();
	}

	/**
	 * Method to remove a balance from an account.
	 * @param account The account to remove the balance from.
	 * @param amount The amount to remove from the account.
	 * @return true if the transaction was successful.
	 */
	public static boolean remove(Account account, double amount) {
		EconomyTransaction transaction = account.withdrawAsync(new BigDecimal(amount)).join();

		return transaction.successful();
	}

	/**
	 * Method to transfer a balance from one account to another.
	 * @param sender The account that is sending the balance.
	 * @param receiver The account that is receiving the balance.
	 * @param amount The amount to be transferred.
	 * @return true if the transaction was successful.
	 */
	public static boolean transfer(Account sender, Account receiver, double amount) {
		boolean removedMoney = remove(sender, amount);
		boolean addMoney = add(receiver, amount);
//		EconomyTransferTransaction transaction = sender.transferAsync(receiver, new BigDecimal(amount)).join();

		if (!removedMoney && addMoney) {
			remove(receiver, amount);
		}

		if (removedMoney && !addMoney) {
			add(sender, amount);
		}

		return removedMoney && addMoney;
	}
}
