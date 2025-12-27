public FinancialProfile createOrUpdate(FinancialProfile profile) {
    // RECTIFICATION: Add validation for the credit score range
    if (profile.getCreditScore() != null && 
       (profile.getCreditScore() < 300 || profile.getCreditScore() > 850)) {
        throw new BadRequestException("Invalid credit score: " + profile.getCreditScore());
    }

    // Standard logic to save/update
    return financialProfileRepository.save(profile);
}