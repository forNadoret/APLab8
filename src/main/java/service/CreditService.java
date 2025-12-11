package service;

import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.BankStorage;

import java.util.ArrayList;
import java.util.List;

public class CreditService {
    private static final Logger logger = LoggerFactory.getLogger(CreditService.class);

    private List<Bank> banks;
    private BankStorage storage;
    private Selection selection;

    public CreditService(BankStorage storage) {
        this.storage = storage;
        this.selection = new Selection();
        this.banks = new ArrayList<>();
        loadData();
    }

    public void loadData() {
        this.banks = storage.loadBanks();
        logger.info("Attempting to load bank data...");
        if (this.banks == null || this.banks.isEmpty()) {
            logger.warn("Bank data is empty or missing. Initializing test data.");

            this.banks = new ArrayList<>();
            Bank b1 = new Bank("B1", "PrivatBank");
            b1.addOffer(new PersonalLoan("PL1", 18.0, 1000, 50000, 6, 24, true, false, "General Needs"));
            b1.addOffer(new CarLoan("CL1", 12.5, 100000, 500000, 12, 60, true, true, true));
            b1.addOffer(new MortgageLoan("ML1", 7.9, 500000, 2000000, 120, 360, true, true, "Apartment"));
            this.banks.add(b1);
            Bank b2 = new Bank("B2", "MonoBank");
            b2.addOffer(new PersonalLoan("PL2", 0.1, 500, 25000, 2, 12, true, false, "Installment"));
            b2.addOffer(new CarLoan("CL2", 15.0, 4000, 15000, 6, 36, true, false, false));
            this.banks.add(b2);
            System.out.println("Test data created: 2 Banks, 5 Offers.");
            saveData();

            logger.info("Test data initialized and saved.");
        } else {
            logger.info("Successfully loaded {} banks from file.", banks.size());
        }
    }

    public void saveData() {
        storage.saveBanks(this.banks);
        logger.info("Data save requested.");
    }

    public void createOffer(String bankId, CreditOffer offer) {
        Bank bank = findBankById(bankId);
        if (bank != null) {
            bank.addOffer(offer);
            logger.info("Created new offer '{}' in bank '{}'", offer.getId(), bank.getName());
            System.out.println("Offer added to bank " + bank.getName());
        } else {
            logger.error("CRITICAL ERROR: Failed to create offer. Bank not found: {}", bankId);
            System.out.println("Bank not found: " + bankId);
        }
    }

    public List<CreditOffer> searchOffers(SearchCriteria criteria) {
        logger.debug("Searching offers with criteria: Rate<={}, Amount>={}, Bank={}",
                criteria.getMaxRate(), criteria.getMinAmount(), criteria.getBankName());

        List<CreditOffer> result = new ArrayList<>();
        for (Bank bank : banks) {
            if (criteria.getBankName() != null && !bank.getName().equalsIgnoreCase(criteria.getBankName())) continue;

            for (CreditOffer offer : bank.getOffers()) {
                if (criteria.getMaxRate() != null && offer.getInterestRate() > criteria.getMaxRate()) continue;
                if (criteria.getMinAmount() != null && offer.getMaxAmount() < criteria.getMinAmount()) continue;
                result.add(offer);
            }
        }
        return result;
    }

    public boolean updateOffer(String offerId, OfferUpdate data) {
        CreditOffer offer = findOfferById(offerId);
        if (offer == null)
        {
            logger.warn("Update failed. Offer ID '{}' not found.", offerId);
            return false;
        }

        if (data.getInterestRate() != null) offer.setInterestRate(data.getInterestRate());
        if (data.getMinAmount() != null) offer.setMinAmount(data.getMinAmount());
        if (data.getMaxAmount() != null) offer.setMaxAmount(data.getMaxAmount());

        logger.info("Offer '{}' updated successfully.", offerId);
        return true;
    }

    public boolean deleteOffer(String offerId) {
        for (Bank bank : banks) {
            if (bank.removeOffer(offerId))
            {
                logger.info("Offer '{}' deleted from bank '{}'.", offerId, bank.getName());
                return true;
            }
        }
        logger.warn("Delete failed. Offer ID '{}' not found.", offerId);
        return false;
    }

    public void addToSelection(String offerId) {
        CreditOffer offer = findOfferById(offerId);
        if (offer != null) {
            selection.addOffer(offer);
            logger.info("Offer '{}' added to basket.", offerId);
            System.out.println("Offer added to selection.");
        } else {
            logger.warn("Failed to add to basket. Offer '{}' not found.", offerId);
            System.out.println("Offer not found.");
        }
    }

    public List<CreditOffer> viewSelection() {
        return selection.getSelectedOffers();
    }

    public List<Bank> listAllBanks() {
        return banks;
    }

    public CreditOffer findOfferById(String offerId) {
        for (Bank bank : banks) {
            for (CreditOffer offer : bank.getOffers()) {
                if (offer.getId().equals(offerId)) return offer;
            }
        }
        return null;
    }

    private Bank findBankById(String id) {
        return banks.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }
}