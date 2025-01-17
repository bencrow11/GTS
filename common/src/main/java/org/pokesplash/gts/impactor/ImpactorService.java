package org.pokesplash.gts.impactor;

import net.impactdev.impactor.api.economy.EconomyService;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.currency.Currency;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;
import org.pokesplash.gts.api.economy.GtsEconomy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class to interact with the Impactor API
 */
public class ImpactorService implements GtsEconomy {

	// The impactor service
	private static EconomyService service = EconomyService.instance();

	// The currency used for this mod.
	private static Currency currency = service.currencies().primary();

	/**
	 * Method to get the account of a player.
	 * @param uuid The UUID of the player.
	 * @return The account of the player.
	 */

	private Account getAccount(UUID uuid) {
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
	@Override
	public boolean add(UUID account, double amount) {

		Account acc = getAccount(account);

		EconomyTransaction transaction = acc.deposit(new BigDecimal(amount));

		return transaction.successful();
	}

	/**
	 * Method to remove a balance from an account.
	 * @param account The account to remove the balance from.
	 * @param amount The amount to remove from the account.
	 * @return true if the transaction was successful.
	 */
	@Override
	public boolean remove(UUID account, double amount) {

		Account acc = getAccount(account);

		EconomyTransaction transaction = acc.withdraw(new BigDecimal(amount));

		return transaction.successful();
	}

	@Override
	public double balance(UUID player) {
		Account acc = getAccount(player);

		return acc.balance().doubleValue();
	}
}
