package org.pokesplash.gts.util;

import net.impactdev.impactor.api.economy.EconomyService;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.currency.Currency;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;
import net.impactdev.impactor.api.economy.transactions.EconomyTransferTransaction;
import net.kyori.adventure.key.Key;

import java.math.BigDecimal;
import java.util.UUID;

public class ImpactorService {

	private EconomyService service;
	private Currency currency;

	public ImpactorService(Key currencyKey) {
		service = EconomyService.instance();

		currency = currencyKey == null ? service.currencies().primary() :
				service.currencies().currency(currencyKey).orElse(service.currencies().primary());
	}

	public Account getAccount(UUID uuid) {
		if (!service.hasAccount(uuid).join()) {
			return null;
		}
		return service.account(currency, uuid).join();
	}

	public boolean add(Account account, double amount) {
		EconomyTransaction transaction = account.depositAsync(new BigDecimal(amount)).join();

		return transaction.successful();
	}

	public boolean remove(Account account, double amount) {
		EconomyTransaction transaction = account.withdrawAsync(new BigDecimal(amount)).join();

		return transaction.successful();
	}

	public boolean transfer(Account sender, Account receiver, double amount) {
		EconomyTransferTransaction transaction = sender.transferAsync(receiver, new BigDecimal(amount)).join();

		return transaction.successful();
	}
}
