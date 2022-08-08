package bg.softuni.fitcom.models.validation;

import bg.softuni.fitcom.repositories.AccountRepository;
import bg.softuni.fitcom.repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotOccupiedValidator implements ConstraintValidator<NotOccupied, String> {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public NotOccupiedValidator(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.userRepository.findByEmail(value).isEmpty() // user is not registered
                && this.accountRepository.findByEmail(value).isEmpty(); // user has not created an account that waits to be confirmed
    }
}
