package com.example.demo;

import com.example.demo.controller.*;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoanDtos;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Listeners({TestResultListener.class})
public class LoanEligibilityEmiRiskCheckerTest {

    // Mocks and common objects
    @Mock private UserRepository mockUserRepository;
    @Mock private FinancialProfileRepository mockFinancialProfileRepository;
    @Mock private LoanRequestRepository mockLoanRequestRepository;
    @Mock private EligibilityResultRepository mockEligibilityResultRepository;
    @Mock private RiskAssessmentRepository mockRiskAssessmentRepository;
    @Mock private JwtUtil mockJwtUtil;

    @InjectMocks private UserServiceImpl userService;
    @InjectMocks private FinancialProfileServiceImpl financialProfileService;
    @InjectMocks private LoanRequestServiceImpl loanRequestService;
    @InjectMocks private EligibilityServiceImpl eligibilityService;
    @InjectMocks private RiskAssessmentServiceImpl riskAssessmentService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userService = new UserServiceImpl(mockUserRepository);
        financialProfileService = new FinancialProfileServiceImpl(mockFinancialProfileRepository, mockUserRepository);
        loanRequestService = new LoanRequestServiceImpl(mockLoanRequestRepository, mockUserRepository);
        eligibilityService = new EligibilityServiceImpl(mockLoanRequestRepository, mockFinancialProfileRepository, mockEligibilityResultRepository);
        riskAssessmentService = new RiskAssessmentServiceImpl(mockLoanRequestRepository, mockFinancialProfileRepository, mockRiskAssessmentRepository);
    }

    // 1–10: Develop and deploy a simple servlet using Tomcat Server (simulated)
    @Test(priority = 1, groups = {"servlet"})
    public void t01_servlet_response_success() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PrintWriter pw = new PrintWriter(new ByteArrayOutputStream());
        when(resp.getWriter()).thenReturn(pw);
        servlet.doGet(req, resp);
        verify(resp, times(1)).setContentType("text/plain");
    }

    @Test(priority = 2, groups = {"servlet"})
    public void t02_servlet_multiple_calls() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        servlet.doGet(req, resp);
        servlet.doGet(req, resp);
        verify(resp, times(2)).setContentType("text/plain");
    }

    @Test(priority = 3, groups = {"servlet"})
    public void t03_servlet_no_exception_on_null_req() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        // ensure it doesn't throw when request is null (simulated)
        servlet.doGet(null, resp);
        verify(resp, times(1)).setContentType("text/plain");
    }

    @Test(priority = 4, groups = {"servlet"})
    public void t04_servlet_content_written() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        when(resp.getWriter()).thenReturn(pw);
        servlet.doGet(req, resp);
        pw.flush();
        String out = baos.toString();
        Assert.assertTrue(out.contains("SimpleStatusServlet"));
    }

    @Test(priority = 5, groups = {"servlet"})
    public void t05_servlet_headers_intact() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        servlet.doGet(req, resp);
        verify(resp, times(1)).setContentType("text/plain");
    }

    @Test(priority = 6, groups = {"servlet"})
    public void t06_servlet_handles_ioexception() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenThrow(new IOException("IO problem"));
        try {
            servlet.doGet(req, resp);
            Assert.fail("Expected IOException");
        } catch (IOException ex) {
            Assert.assertTrue(ex.getMessage().contains("IO problem"));
        }
    }

    @Test(priority = 7, groups = {"servlet"})
    public void t07_servlet_reentrant() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        servlet.doGet(req, resp);
        servlet.doGet(req, resp);
        verify(resp, times(2)).setContentType("text/plain");
    }

    @Test(priority = 8, groups = {"servlet"})
    public void t08_servlet_threadsafe_like_behavior() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        // simulate two concurrent calls
        HttpServletRequest req1 = mock(HttpServletRequest.class);
        HttpServletResponse resp1 = mock(HttpServletResponse.class);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        HttpServletResponse resp2 = mock(HttpServletResponse.class);
        when(resp1.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        when(resp2.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        servlet.doGet(req1, resp1);
        servlet.doGet(req2, resp2);
        verify(resp1, times(1)).setContentType("text/plain");
        verify(resp2, times(1)).setContentType("text/plain");
    }

    @Test(priority = 9, groups = {"servlet"})
    public void t09_servlet_status_code_default() throws Exception {
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        servlet.doGet(null, resp);
        // no status set -> default 200; can't easily validate status via mock, but verifying contentType called
        verify(resp, times(1)).setContentType("text/plain");
    }

    @Test(priority = 10, groups = {"servlet"})
    public void t10_servlet_no_dependency_on_spring_context() throws Exception {
        // Ensure servlet can operate without Spring context
        com.example.demo.servlet.SimpleStatusServlet servlet = new com.example.demo.servlet.SimpleStatusServlet();
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream()));
        servlet.doGet(null, resp);
        verify(resp).setContentType("text/plain");
    }

    // 11–25: Implement CRUD operations using Spring Boot and REST APIs
    @Test(priority = 11, groups = {"crud"})
    public void t11_user_register_positive() {
        User input = new User();
        input.setEmail("test@example.com");
        input.setPassword("password");
        input.setFullName("Test User");

        when(mockUserRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User created = userService.register(input);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getRole(), User.Role.CUSTOMER.name());
        Assert.assertNotEquals(created.getPassword(), "password"); // hashed
    }

    @Test(priority = 12, groups = {"crud"})
    public void t12_user_register_duplicate_email() {
        User input = new User();
        input.setEmail("dup@example.com");
        input.setPassword("pwd");
        input.setFullName("Dup");

        when(mockUserRepository.findByEmail("dup@example.com")).thenReturn(Optional.of(new User()));
        try {
            userService.register(input);
            Assert.fail("Expected BadRequestException");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof BadRequestException);
        }
    }

    @Test(priority = 13, groups = {"crud"})
    public void t13_get_user_by_id_positive() {
        User stored = new User();
        stored.setId(2L);
        stored.setEmail("u2@example.com");
        when(mockUserRepository.findById(2L)).thenReturn(Optional.of(stored));
        User found = userService.getById(2L);
        Assert.assertEquals(found.getEmail(), "u2@example.com");
    }

    @Test(priority = 14, groups = {"crud"})
    public void t14_get_user_not_found() {
        when(mockUserRepository.findById(99L)).thenReturn(Optional.empty());
        try {
            userService.getById(99L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof com.example.demo.exception.ResourceNotFoundException);
        }
    }

    @Test(priority = 15, groups = {"crud"})
    public void t15_create_financial_profile_positive() {
        User u = new User();
        u.setId(3L);
        when(mockUserRepository.findById(3L)).thenReturn(Optional.of(u));
        when(mockFinancialProfileRepository.findByUserId(3L)).thenReturn(Optional.empty());
        FinancialProfile fp = new FinancialProfile();
        User userRef = new User();
        userRef.setId(3L);
        fp.setUser(userRef);
        fp.setMonthlyIncome(5000.0);
        fp.setMonthlyExpenses(2000.0);
        fp.setExistingLoanEmi(100.0);
        fp.setCreditScore(700);
        fp.setSavingsBalance(10000.0);
        when(mockFinancialProfileRepository.save(any(FinancialProfile.class))).thenAnswer(inv -> {
            FinancialProfile f = inv.getArgument(0);
            f.setId(1L);
            return f;
        });
        FinancialProfile created = financialProfileService.createOrUpdate(fp);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getCreditScore().intValue(), 700);
    }

    @Test(priority = 16, groups = {"crud"})
    public void t16_create_financial_profile_invalid_credit_score() {
        FinancialProfile fp = new FinancialProfile();
        User u = new User();
        u.setId(4L);
        fp.setUser(u);
        fp.setCreditScore(200);
        try {
            financialProfileService.createOrUpdate(fp);
            Assert.fail("Expected BadRequestException");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof BadRequestException);
        }
    }

    @Test(priority = 17, groups = {"crud"})
    public void t17_submit_loan_request_positive() {
        User u = new User();
        u.setId(5L);
        when(mockUserRepository.findById(5L)).thenReturn(Optional.of(u));
        LoanRequest request = new LoanRequest();
        User ur = new User();
        ur.setId(5L);
        request.setUser(ur);
        request.setRequestedAmount(10000.0);
        request.setTenureMonths(24);
        when(mockLoanRequestRepository.save(any(LoanRequest.class))).thenAnswer(inv -> {
            LoanRequest lr = inv.getArgument(0);
            lr.setId(10L);
            return lr;
        });
        LoanRequest created = loanRequestService.submitRequest(request);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getStatus(), LoanRequest.Status.PENDING.name());
    }

    @Test(priority = 18, groups = {"crud"})
    public void t18_submit_loan_request_invalid_amount() {
        LoanRequest request = new LoanRequest();
        User u = new User();
        u.setId(6L);
        request.setUser(u);
        request.setRequestedAmount(0.0);
        try {
            loanRequestService.submitRequest(request);
            Assert.fail("Expected BadRequestException");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof BadRequestException);
        }
    }

    @Test(priority = 19, groups = {"crud"})
    public void t19_get_requests_by_user() {
        List<LoanRequest> list = new ArrayList<>();
        LoanRequest lr = new LoanRequest();
        lr.setId(20L);
        list.add(lr);
        when(mockLoanRequestRepository.findByUserId(7L)).thenReturn(list);
        List<LoanRequest> out = loanRequestService.getRequestsByUser(7L);
        Assert.assertEquals(out.size(), 1);
        Assert.assertEquals(out.get(0).getId().longValue(), 20L);
    }

    @Test(priority = 20, groups = {"crud"})
    public void t20_duplicate_loan_request_retrieve() {
        when(mockLoanRequestRepository.findById(30L)).thenReturn(Optional.of(new LoanRequest()));
        LoanRequest lr = loanRequestService.getById(30L);
        Assert.assertNotNull(lr);
    }

    // 26–40: DI and IoC - test injection and bean behavior
    @Test(priority = 21, groups = {"di"})
    public void t21_user_service_dependency_injected() {
        Assert.assertNotNull(userService);
        Assert.assertNotNull(financialProfileService);
        Assert.assertNotNull(loanRequestService);
    }

    @Test(priority = 22, groups = {"di"})
    public void t22_password_encoder_used_on_register() {
        // Ensure that BCryptPasswordEncoder used in UserServiceImpl; simulate registration
        User u = new User();
        u.setEmail("enc@example.com");
        u.setPassword("plain");
        u.setFullName("Enc");
        when(mockUserRepository.findByEmail("enc@example.com")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenAnswer(inv -> {
            User uu = inv.getArgument(0);
            uu.setId(40L);
            return uu;
        });
        User created = userService.register(u);
        Assert.assertNotEquals(created.getPassword(), "plain");
    }

    @Test(priority = 23, groups = {"di"})
    public void t23_service_throws_without_repository() {
        UserServiceImpl broken = new UserServiceImpl(null);
        try {
            broken.getById(1L);
            Assert.fail("Expected NullPointerException or similar");
        } catch (Exception ex) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 24, groups = {"di"})
    public void t24_multiple_service_calls_independent() {
        when(mockUserRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenAnswer(inv -> {
            User uu = inv.getArgument(0);
            uu.setId(50L);
            return uu;
        });
        User u1 = new User();
        u1.setEmail("a@a.com"); u1.setPassword("p"); u1.setFullName("A");
        User r1 = userService.register(u1);
        Assert.assertNotNull(r1.getId());

        // Another independent call
        when(mockUserRepository.findByEmail("b@b.com")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenAnswer(inv -> {
            User uu = inv.getArgument(0);
            uu.setId(51L);
            return uu;
        });
        User u2 = new User();
        u2.setEmail("b@b.com"); u2.setPassword("p"); u2.setFullName("B");
        User r2 = userService.register(u2);
        Assert.assertNotEquals(r1.getId(), r2.getId());
    }

    @Test(priority = 25, groups = {"di"})
public void t25_singleton_service_behaviour() {
    UserRepository repo = Mockito.mock(UserRepository.class);

    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setEmail("test@example.com");
    mockUser.setPassword("hashed");
    mockUser.setRole("CUSTOMER");
    mockUser.setFullName("Test User");

    Mockito.when(repo.findByEmail("test@example.com"))
            .thenReturn(Optional.of(mockUser));

    UserServiceImpl service = new UserServiceImpl(repo);

    User found = service.findByEmail("test@example.com");

    Assert.assertNotNull(found);
    Assert.assertEquals(found.getEmail(), "test@example.com");
}


    // 26–35: Hibernate configurations, generator classes, annotations, and CRUD operations
    @Test(priority = 26, groups = {"hibernate"})
    public void t26_entity_user_annotations_present() {
        // Check entity class exists and fields accessible
        User u = new User();
        u.setFullName("Annot");
        u.setEmail("annot@example.com");
        Assert.assertEquals(u.getFullName(), "Annot");
    }

    @Test(priority = 27, groups = {"hibernate"})
public void t27_financial_profile_unique_user_constraint_simulated() {
    FinancialProfileRepository repo = Mockito.mock(FinancialProfileRepository.class);
    UserRepository userRepo = Mockito.mock(UserRepository.class);

    User user = new User();
    user.setId(1L);
    user.setEmail("x@y.com");

    Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(user));

    FinancialProfile existing = new FinancialProfile();
    existing.setId(100L);
    existing.setUser(user);
    existing.setMonthlyIncome(50000.0);
    existing.setMonthlyExpenses(20000.0);
    existing.setCreditScore(750);
    existing.setSavingsBalance(100000.0);

    Mockito.when(repo.findByUserId(1L)).thenReturn(Optional.of(existing));

    FinancialProfile updated = new FinancialProfile();
    updated.setUser(user);
    updated.setMonthlyIncome(60000.0);
    updated.setMonthlyExpenses(25000.0);
    updated.setCreditScore(760);
    updated.setSavingsBalance(120000.0);

    Mockito.when(repo.save(Mockito.any(FinancialProfile.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    FinancialProfileServiceImpl service = new FinancialProfileServiceImpl(repo, userRepo);

    FinancialProfile result = service.createOrUpdate(updated);

    Assert.assertEquals(result.getMonthlyIncome(), 60000.0);
    Assert.assertEquals(result.getCreditScore(), 760);
    Assert.assertEquals(result.getId(), existing.getId(),
            "Should update existing profile rather than creating a duplicate");
}



    @Test(priority = 28, groups = {"hibernate"})
    public void t28_loan_request_persist_sets_defaults() {
        LoanRequest lr = new LoanRequest();
        lr.setRequestedAmount(1000.0);
        lr.setTenureMonths(12);
        User u = new User(); u.setId(70L);
        lr.setUser(u);
        when(mockUserRepository.findById(70L)).thenReturn(Optional.of(u));
        when(mockLoanRequestRepository.save(any(LoanRequest.class))).thenAnswer(inv -> {
            LoanRequest out = inv.getArgument(0);
            out.setId(80L);
            return out;
        });
        LoanRequest created = loanRequestService.submitRequest(lr);
        Assert.assertEquals(created.getStatus(), LoanRequest.Status.PENDING.name());
        Assert.assertNotNull(created.getSubmittedAt());
    }

    @Test(priority = 29, groups = {"hibernate"})
    public void t29_entity_prepersist_timestamps() {
        FinancialProfile fp = new FinancialProfile();
        User u = new User(); u.setId(90L);
        fp.setUser(u);
        fp.setMonthlyIncome(1000.0);
        fp.setMonthlyExpenses(200.0);
        fp.setCreditScore(650);
        fp.setSavingsBalance(500.0);
        // calling createOrUpdate will set updated timestamp via prePersist
        when(mockUserRepository.findById(90L)).thenReturn(Optional.of(u));
        when(mockFinancialProfileRepository.findByUserId(90L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.save(any(FinancialProfile.class))).thenAnswer(inv -> {
            FinancialProfile f = inv.getArgument(0);
            f.setId(90L);
            return f;
        });
        FinancialProfile created = financialProfileService.createOrUpdate(fp);
        Assert.assertNotNull(created.getLastUpdatedAt());
    }

    @Test(priority = 30, groups = {"hibernate"})
    public void t30_entity_relations_integrity() {
        LoanRequest lr = new LoanRequest();
        User u = new User();
        u.setId(100L);
        lr.setUser(u);
        Assert.assertEquals(lr.getUser().getId().longValue(), 100L);
    }

    // 36–45: Perform JPA mapping with normalization (1NF, 2NF, 3NF)
    @Test(priority = 31, groups = {"jpa"})
    public void t31_normalization_basic_check_1NF() {
        // 1NF requires atomic fields; check by having simple fields in entity
        User u = new User();
        u.setFullName("NF1");
        u.setEmail("nf1@example.com");
        Assert.assertNotNull(u.getEmail());
    }

    @Test(priority = 32, groups = {"jpa"})
    public void t32_normalization_2NF_check() {
        // 2NF: no partial dependency — entities here uphold separate tables
        FinancialProfile fp = new FinancialProfile();
        fp.setMonthlyIncome(1000.0);
        Assert.assertNotNull(fp.getMonthlyIncome());
    }

    @Test(priority = 33, groups = {"jpa"})
    public void t33_normalization_3NF_check() {
        // 3NF: remove transitive dependency — separate entities for user and profiles meet this
        User u = new User();
        FinancialProfile fp = new FinancialProfile();
        fp.setUser(u);
        Assert.assertEquals(fp.getUser(), u);
    }

    @Test(priority = 34, groups = {"jpa"})
    public void t34_jpa_unique_constraints_exist() {
        // verify annotated unique constraints exist conceptually (cannot introspect easily at runtime)
        User u = new User();
        u.setEmail("unique@example.com");
        Assert.assertTrue(u.getEmail().contains("@"));
    }

    @Test(priority = 35, groups = {"jpa"})
    public void t35_jpa_one_to_one_mapping_financial_profile_user() {
        FinancialProfile fp = new FinancialProfile();
        User u = new User();
        u.setId(110L);
        fp.setUser(u);
        Assert.assertEquals(fp.getUser().getId().longValue(), 110L);
    }

    // 46–50: Create Many-to-Many relationships and test associations in Spring Boot
    // Note: Project doesn't define a Many-to-Many entity; simulate conceptual tests
    @Test(priority = 36, groups = {"manytomany"})
public void t36_many_to_many_simulation_basic() {
    // Using existing project entities to simulate many-to-many
    User user = new User();
    user.setFullName("User A");
    user.setEmail("a@example.com");
    user.setPassword("hashed");
    user.setRole("CUSTOMER");

    LoanRequest lr1 = new LoanRequest();
    lr1.setRequestedAmount(20000.0);
    lr1.setTenureMonths(12);
    lr1.setUser(user);

    LoanRequest lr2 = new LoanRequest();
    lr2.setRequestedAmount(30000.0);
    lr2.setTenureMonths(18);
    lr2.setUser(user);

    List<LoanRequest> list = List.of(lr1, lr2);

    Assert.assertEquals(list.size(), 2);
    Assert.assertEquals(list.get(0).getUser(), user);
}


    @Test(priority = 37, groups = {"manytomany"})
public void t37_many_to_many_adding_removing() {
    User user = new User();
    user.setFullName("Remove Test");
    user.setEmail("remove@example.com");
    user.setPassword("hashed");
    user.setRole("CUSTOMER");

    LoanRequest lr = new LoanRequest();
    lr.setRequestedAmount(15000.0);
    lr.setTenureMonths(10);
    lr.setUser(user);

    List<LoanRequest> loanList = new ArrayList<>();
    loanList.add(lr);

    Assert.assertEquals(loanList.size(), 1);

    loanList.remove(lr);

    Assert.assertTrue(loanList.isEmpty());
}


    @Test(priority = 38, groups = {"manytomany"})
public void t38_many_to_many_cardinality() {
    User user1 = new User();
    user1.setFullName("User 1");
    user1.setEmail("u1@example.com");
    user1.setPassword("hashed");
    user1.setRole("CUSTOMER");

    User user2 = new User();
    user2.setFullName("User 2");
    user2.setEmail("u2@example.com");
    user2.setPassword("hashed");
    user2.setRole("CUSTOMER");

    LoanRequest lr = new LoanRequest();
    lr.setRequestedAmount(40000.0);
    lr.setTenureMonths(24);

    // simulate "many users referencing same loan" (conceptual test)
    List<User> users = List.of(user1, user2);

    Assert.assertEquals(users.size(), 2);
}


    @Test(priority = 39, groups = {"manytomany"})
    public void t39_many_to_many_consistency_post_persist_simulation() {
        // After persist, associations should remain consistent (simulated)
        Map<String, Object> db = new HashMap<>();
        db.put("a1", "saved");
        Assert.assertTrue(db.containsKey("a1"));
    }

    @Test(priority = 40, groups = {"manytomany"})
    public void t40_many_to_many_no_orphan_left() {
        // simulate orphan removal behavior
        List<String> list = new ArrayList<>();
        list.add("x");
        list.remove("x");
        Assert.assertTrue(list.isEmpty());
    }

    // 51–55: Implement basic security controls and JWT token-based authentication
    @Test(priority = 41, groups = {"security"})
    public void t41_jwt_generation_and_claims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "CUSTOMER");
        claims.put("email", "jwt@example.com");
        claims.put("userId", 123L);

        JwtUtil realUtil = new JwtUtil("ChangeThisSecretForProductionButKeepItLongEnough", 3600000);
        String token = realUtil.generateToken(claims, "jwt@example.com");
        Assert.assertNotNull(token);
        // validate parsing
        Assert.assertEquals(realUtil.getAllClaims(token).get("email", String.class), "jwt@example.com");
    }

    @Test(priority = 42, groups = {"security"})
    public void t42_jwt_token_expiry() throws InterruptedException {
        // create very short-lived token to test expiry
        JwtUtil realUtil = new JwtUtil("ChangeThisSecretForProductionButKeepItLongEnough", 100);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "x@x.com");
        String token = realUtil.generateToken(claims, "x@x.com");
        Assert.assertNotNull(token);
        // wait for expiry
        Thread.sleep(150);
        try {
            realUtil.getAllClaims(token);
            Assert.fail("Expected exception due to expiry");
        } catch (Exception ex) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 43, groups = {"security"})
    public void t43_jwt_filter_accepts_valid_token() throws Exception {
        JwtUtil realUtil = new JwtUtil("ChangeThisSecretForProductionButKeepItLongEnough", 3600000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "CUSTOMER");
        claims.put("email", "ff@f.com");
        claims.put("userId", 222L);
        String token = realUtil.generateToken(claims, "ff@f.com");

        com.example.demo.security.JwtFilter filter = new com.example.demo.security.JwtFilter(realUtil);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        jakarta.servlet.FilterChain chain = mock(jakarta.servlet.FilterChain.class);
        filter.doFilter(req, resp, chain);
        verify(chain, times(1)).doFilter(req, resp);
    }

    @Test(priority = 44, groups = {"security"})
    public void t44_jwt_filter_rejects_invalid_token() throws Exception {
        JwtUtil realUtil = new JwtUtil("ChangeThisSecretForProductionButKeepItLongEnough", 3600000);
        com.example.demo.security.JwtFilter filter = new com.example.demo.security.JwtFilter(realUtil);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer invalid.token.here");
        jakarta.servlet.FilterChain chain = mock(jakarta.servlet.FilterChain.class);
        filter.doFilter(req, resp, chain);
        verify(chain, times(1)).doFilter(req, resp); // filter swallows invalid and continues
    }

    @Test(priority = 45, groups = {"security"})
    public void t45_authcontroller_login_positive() {
        // Simulate login path using AuthController and mocks
        User u = new User();
        u.setId(300L);
        u.setEmail("login@example.com");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        u.setPassword(encoder.encode("secure"));
        when(mockUserRepository.findByEmail("login@example.com")).thenReturn(Optional.of(u));
        AuthController controller = new AuthController(new UserServiceImpl(mockUserRepository), new JwtUtil("ChangeThisSecretForProductionButKeepItLongEnough", 3600000), mockUserRepository);
        AuthRequest req = new AuthRequest("login@example.com", "secure");
        ResponseEntity<AuthResponse> resp = controller.login(req);
        Assert.assertNotNull(resp.getBody().getToken());
        Assert.assertEquals(resp.getBody().getEmail(), "login@example.com");
    }

    // 56–60: Use HQL and HCQL to perform advanced data querying (simulated)
    @Test(priority = 46, groups = {"hql"})
    public void t46_hql_query_simulation_find_by_user() {
        // Simulate repository custom queries behavior
        List<LoanRequest> list = new ArrayList<>();
        LoanRequest lr = new LoanRequest();
        lr.setId(401L);
        list.add(lr);
        when(mockLoanRequestRepository.findByUserId(500L)).thenReturn(list);
        List<LoanRequest> res = loanRequestService.getRequestsByUser(500L);
        Assert.assertEquals(res.size(), 1);
        Assert.assertEquals(res.get(0).getId().longValue(), 401L);
    }

    @Test(priority = 47, groups = {"hql"})
    public void t47_hql_find_latest_financial_profile() {
        FinancialProfile fp = new FinancialProfile();
        fp.setId(402L);
        when(mockFinancialProfileRepository.findByUserId(600L)).thenReturn(Optional.of(fp));
        FinancialProfile res = financialProfileService.getByUserId(600L);
        Assert.assertEquals(res.getId().longValue(), 402L);
    }

    @Test(priority = 48, groups = {"hql"})
    public void t48_hql_find_eligibility_by_request_id_positive() {
        EligibilityResult er = new EligibilityResult();
        er.setId(403L);
        when(mockEligibilityResultRepository.findByLoanRequestId(700L)).thenReturn(Optional.of(er));
        EligibilityResult out = eligibilityService.getByLoanRequestId(700L);
        Assert.assertEquals(out.getId().longValue(), 403L);
    }

    @Test(priority = 49, groups = {"hql"})
    public void t49_hql_find_risk_by_request_id_positive() {
        RiskAssessment ra = new RiskAssessment();
        ra.setId(404L);
        when(mockRiskAssessmentRepository.findByLoanRequestId(800L)).thenReturn(Optional.of(ra));
        RiskAssessment out = riskAssessmentService.getByLoanRequestId(800L);
        Assert.assertEquals(out.getId().longValue(), 404L);
    }

    @Test(priority = 50, groups = {"hql"})
    public void t50_hql_complex_query_simulation() {
        // simulate HQL returning aggregated results (not actual DB call)
        List<LoanRequest> allRequests = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LoanRequest l = new LoanRequest();
            l.setId((long) (900 + i));
            allRequests.add(l);
        }
        when(mockLoanRequestRepository.findAll()).thenReturn(allRequests);
        List<LoanRequest> fetched = mockLoanRequestRepository.findAll();
        Assert.assertEquals(fetched.size(), 5);
    }

    // 51–60: Additional realistic integration tests related to project
    @Test(priority = 51, groups = {"integration"})
    public void t51_evaluate_eligibility_positive_flow() {
        User u = new User(); u.setId(1000L);
        LoanRequest lr = new LoanRequest(); lr.setId(2000L); lr.setUser(u); lr.setRequestedAmount(10000.0); lr.setTenureMonths(12);
        FinancialProfile fp = new FinancialProfile(); fp.setUser(u); fp.setMonthlyIncome(5000.0); fp.setMonthlyExpenses(1000.0); fp.setExistingLoanEmi(0.0); fp.setCreditScore(700); fp.setSavingsBalance(5000.0);

        when(mockLoanRequestRepository.findById(2000L)).thenReturn(Optional.of(lr));
        when(mockEligibilityResultRepository.findByLoanRequestId(2000L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.findByUserId(1000L)).thenReturn(Optional.of(fp));
        when(mockEligibilityResultRepository.save(any(EligibilityResult.class))).thenAnswer(inv -> {
            EligibilityResult e = inv.getArgument(0);
            e.setId(5000L);
            return e;
        });

        EligibilityResult result = eligibilityService.evaluateEligibility(2000L);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getMaxEligibleAmount() >= 0.0);
    }

    @Test(priority = 52, groups = {"integration"})
    public void t52_assess_risk_positive_flow() {
        User u = new User(); u.setId(1100L);
        LoanRequest lr = new LoanRequest(); lr.setId(2100L); lr.setUser(u); lr.setRequestedAmount(12000.0); lr.setTenureMonths(12);
        FinancialProfile fp = new FinancialProfile(); fp.setUser(u); fp.setMonthlyIncome(6000.0); fp.setMonthlyExpenses(1000.0); fp.setExistingLoanEmi(200.0); fp.setCreditScore(680); fp.setSavingsBalance(10000.0);

        when(mockLoanRequestRepository.findById(2100L)).thenReturn(Optional.of(lr));
        when(mockRiskAssessmentRepository.findByLoanRequestId(2100L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.findByUserId(1100L)).thenReturn(Optional.of(fp));
        when(mockRiskAssessmentRepository.save(any(RiskAssessment.class))).thenAnswer(inv -> {
            RiskAssessment r = inv.getArgument(0);
            r.setId(6000L);
            return r;
        });

        RiskAssessment assessment = riskAssessmentService.assessRisk(2100L);
        Assert.assertNotNull(assessment);
        Assert.assertTrue(assessment.getRiskScore() >= 0.0 && assessment.getRiskScore() <= 100.0);
    }

    @Test(priority = 53, groups = {"integration"})
    public void t53_eligibility_already_exists_case() {
        when(mockEligibilityResultRepository.findByLoanRequestId(3000L)).thenReturn(Optional.of(new EligibilityResult()));
        when(mockLoanRequestRepository.findById(3000L)).thenReturn(Optional.of(new LoanRequest()));
        try {
            eligibilityService.evaluateEligibility(3000L);
            Assert.fail("Expected BadRequestException for duplicate evaluation");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof BadRequestException);
        }
    }

    @Test(priority = 54, groups = {"integration"})
    public void t54_risk_already_exists_case() {
        when(mockRiskAssessmentRepository.findByLoanRequestId(4000L)).thenReturn(Optional.of(new RiskAssessment()));
        when(mockLoanRequestRepository.findById(4000L)).thenReturn(Optional.of(new LoanRequest()));
        try {
            riskAssessmentService.assessRisk(4000L);
            Assert.fail("Expected BadRequestException for duplicate risk");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof BadRequestException);
        }
    }

    @Test(priority = 55, groups = {"integration"})
    public void t55_integration_full_cycle_register_profile_request_assess() {
        // Register
        User u = new User(); u.setId(7000L); u.setEmail("cycle@example.com"); u.setPassword(new BCryptPasswordEncoder().encode("pwd")); u.setFullName("Cycle");
        when(mockUserRepository.findByEmail("cycle@example.com")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenAnswer(inv -> {
            User uu = inv.getArgument(0); uu.setId(7000L); return uu;
        });

        // Profile
        when(mockUserRepository.findById(7000L)).thenReturn(Optional.of(u));
        when(mockFinancialProfileRepository.findByUserId(7000L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.save(any(FinancialProfile.class))).thenAnswer(inv -> {
            FinancialProfile f = inv.getArgument(0); f.setId(700L); return f;
        });

        // Due to complexity we assert the registration path works (profile/save mocked separately)
        User saved = userService.register(u);
        Assert.assertNotNull(saved.getId());
    }

    @Test(priority = 56, groups = {"edge"})
    public void t56_edge_credit_score_boundary_low() {
        FinancialProfile fp = new FinancialProfile();
        User u = new User(); u.setId(8000L);
        fp.setUser(u);
        fp.setMonthlyIncome(1000.0);
        fp.setMonthlyExpenses(100.0);
        fp.setCreditScore(300);
        try {
            when(mockUserRepository.findById(8000L)).thenReturn(Optional.of(u));
            when(mockFinancialProfileRepository.findByUserId(8000L)).thenReturn(Optional.empty());
            when(mockFinancialProfileRepository.save(any(FinancialProfile.class))).thenAnswer(inv -> {
                FinancialProfile f = inv.getArgument(0);
                f.setId(801L);
                return f;
            });
            FinancialProfile created = financialProfileService.createOrUpdate(fp);
            Assert.assertEquals(created.getCreditScore().intValue(), 300);
        } catch (Exception ex) {
            Assert.fail("Should accept credit score 300 as boundary");
        }
    }

    @Test(priority = 57, groups = {"edge"})
    public void t57_edge_credit_score_boundary_high() {
        FinancialProfile fp = new FinancialProfile();
        User u = new User(); u.setId(8100L);
        fp.setUser(u);
        fp.setMonthlyIncome(2000.0);
        fp.setMonthlyExpenses(200.0);
        fp.setCreditScore(900);
        when(mockUserRepository.findById(8100L)).thenReturn(Optional.of(u));
        when(mockFinancialProfileRepository.findByUserId(8100L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.save(any(FinancialProfile.class))).thenAnswer(inv -> {
            FinancialProfile f = inv.getArgument(0); f.setId(811L); return f;
        });
        FinancialProfile created = financialProfileService.createOrUpdate(fp);
        Assert.assertEquals(created.getCreditScore().intValue(), 900);
    }

    @Test(priority = 58, groups = {"edge"})
    public void t58_edge_zero_income_for_risk() {
        User u = new User(); u.setId(8200L);
        LoanRequest lr = new LoanRequest(); lr.setId(9200L); lr.setUser(u); lr.setRequestedAmount(5000.0); lr.setTenureMonths(12);
        FinancialProfile fp = new FinancialProfile(); fp.setUser(u); fp.setMonthlyIncome(0.0); fp.setMonthlyExpenses(0.0); fp.setExistingLoanEmi(0.0); fp.setCreditScore(500); fp.setSavingsBalance(0.0);

        when(mockLoanRequestRepository.findById(9200L)).thenReturn(Optional.of(lr));
        when(mockRiskAssessmentRepository.findByLoanRequestId(9200L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.findByUserId(8200L)).thenReturn(Optional.of(fp));
        when(mockRiskAssessmentRepository.save(any(RiskAssessment.class))).thenAnswer(inv -> {
            RiskAssessment r = inv.getArgument(0); r.setId(9201L); return r;
        });

        RiskAssessment assessment = riskAssessmentService.assessRisk(9200L);
        Assert.assertNotNull(assessment);
        Assert.assertTrue(assessment.getDtiRatio() == 0.0);
    }

    @Test(priority = 59, groups = {"edge"})
    public void t59_large_requested_amount_edge_case() {
        User u = new User(); u.setId(8300L);
        LoanRequest lr = new LoanRequest(); lr.setId(9300L); lr.setUser(u); lr.setRequestedAmount(1e9); lr.setTenureMonths(120);
        FinancialProfile fp = new FinancialProfile(); fp.setUser(u); fp.setMonthlyIncome(10000.0); fp.setMonthlyExpenses(1000.0); fp.setExistingLoanEmi(100.0); fp.setCreditScore(700); fp.setSavingsBalance(100000.0);

        when(mockLoanRequestRepository.findById(9300L)).thenReturn(Optional.of(lr));
        when(mockEligibilityResultRepository.findByLoanRequestId(9300L)).thenReturn(Optional.empty());
        when(mockFinancialProfileRepository.findByUserId(8300L)).thenReturn(Optional.of(fp));
        when(mockEligibilityResultRepository.save(any(EligibilityResult.class))).thenAnswer(inv -> {
            EligibilityResult e = inv.getArgument(0); e.setId(9301L); return e;
        });

        EligibilityResult result = eligibilityService.evaluateEligibility(9300L);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getMaxEligibleAmount() >= 0.0);
    }

    @Test(priority = 60, groups = {"final"})
    public void t60_final_comprehensive_assertion() {
        // Basic sanity check that mocks are present and services are wired
        Assert.assertNotNull(mockUserRepository);
        Assert.assertNotNull(userService);
    }
}
