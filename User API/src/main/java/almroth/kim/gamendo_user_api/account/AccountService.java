package almroth.kim.gamendo_user_api.account;

import almroth.kim.gamendo_user_api.account.dto.SimpleResponse;
import almroth.kim.gamendo_user_api.account.dto.UpdateAccountAdminRequest;
import almroth.kim.gamendo_user_api.account.model.Account;
import almroth.kim.gamendo_user_api.business.BusinessRepository;
import almroth.kim.gamendo_user_api.business.BusinessService;
import almroth.kim.gamendo_user_api.error.customException.EmailAlreadyTakenException;
import almroth.kim.gamendo_user_api.mapper.AccountMapper;
import almroth.kim.gamendo_user_api.role.RoleRepository;
import almroth.kim.gamendo_user_api.role.RoleType;
import com.password4j.BcryptFunction;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BusinessRepository businessRepository;
    private final BusinessService businessService;
    private final RoleRepository roleRepository;

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);
    BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.Y, 11);

    @Autowired
    public AccountService(AccountRepository accountRepository, BusinessRepository businessRepository, RoleRepository roleRepository, @Lazy BusinessService businessService) {

        this.accountRepository = accountRepository;
        this.businessRepository = businessRepository;
        this.roleRepository = roleRepository;
        this.businessService = businessService;
    }

    public List<SimpleResponse> getAccounts() {
        var accounts = accountRepository.findAll();
        ArrayList<SimpleResponse> simpleData = new ArrayList<>();
        for (Account acc : accounts) {
            simpleData.add(mapper.SIMPLE_RESPONSE(acc));
        }
        return simpleData;
    }

    public List<SimpleResponse> getUserAccounts(){
        System.out.println("Getting user accounts only...");
        var accounts = accountRepository.findAll();
        var userAccounts = accounts.stream().filter(account -> account.getRoles().stream().allMatch(role -> role.getName() == RoleType.USER)).toList();
        ArrayList<SimpleResponse> simpleData = new ArrayList<>();
        for (Account acc : userAccounts) {
            simpleData.add(mapper.SIMPLE_RESPONSE(acc));
        }
        System.out.println("Successfully got user accounts only.");
        return simpleData;
    }

    public Set<SimpleResponse> getAccountsByBusiness(String businessName){
        var accounts = accountRepository.findAllByProfile_Business_Name(businessName).orElse(new HashSet<>());
        var simpleAccounts = new HashSet<SimpleResponse>();
        for (var account :
                accounts) {
            simpleAccounts.add(mapper.SIMPLE_RESPONSE(account));
        }
        return simpleAccounts;
    }


    public void removeAccountByUUID(UUID uuid) {
        if (!accountRepository.existsById(uuid)) {
            return;
        }
        accountRepository.deleteById(uuid);
    }

    public SimpleResponse getSimpleAccountByUuid(String uuid) {
        Account account = accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new IllegalStateException("No account with id: " + uuid));
        return mapper.SIMPLE_RESPONSE(account);
    }
    public Account getAccountByUuid(String uuid) {
        return accountRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new IllegalStateException("No account with id: " + uuid));
    }

    public SimpleResponse updateAccount(String accountId, UpdateAccountAdminRequest request) {
        Account account = accountRepository
                .findById(UUID.fromString(accountId))
                .orElseThrow(() -> new IllegalStateException("No account with id: " + accountId));

        if (!Objects.equals(account.getEmail(), request.getEmail())
                && accountRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyTakenException("That email is unavailable.");

        var business = businessService.GetByUuid(request.getBusiness());

        account.setEmail(request.getEmail());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.getProfile().setBusiness(business);

        accountRepository.saveAndFlush(account);
        return mapper.SIMPLE_RESPONSE(account);
    }

    private String generateHashedPassword(String password) {
        return Password.hash(password).with(bcrypt).getResult();
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No account with that email"));
    }
    public boolean doesAccountExistByEmail(String email){
        return  accountRepository.existsByEmail(email);
    }
}
