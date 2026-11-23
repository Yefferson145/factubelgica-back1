package com.factubelgica.api;

import com.factubelgica.api.contexts.billing.models.Invoice;
import com.factubelgica.api.contexts.billing.persistance.IInvoiceRepository;
import com.factubelgica.api.contexts.clients.models.Client;
import com.factubelgica.api.contexts.clients.persistance.IClientRepository;
import com.factubelgica.api.shared.core.JwtService;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IInvoiceRepository invoiceRepository;

	@Autowired
	private IClientRepository clientRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void jwtSecretFromEnvironmentCanGenerateAndValidateTokens() {
		UUID userId = UUID.randomUUID();

		String token = jwtService.generateToken(userId);

		assertEquals(userId, jwtService.validateTokenAndGetSubject(token));
	}

	@Test
	void hikariPoolStaysWithinSupabaseSessionPoolerLimits() {
		HikariDataSource hikari = assertInstanceOf(HikariDataSource.class, dataSource);

		assertEquals(3, hikari.getMaximumPoolSize());
		assertEquals(0, hikari.getMinimumIdle());
		assertTrue(
				hikari.getHikariPoolMXBean().getTotalConnections() <= hikari.getMaximumPoolSize(),
				"HikariCP must never create more connections than maximum-pool-size");
	}

	@Test
	void lazyRelationsAreReadableWithOpenInViewDisabled() {
		int clientId = 9001;
		int infoId = 9001;
		UUID invoiceId = UUID.randomUUID();

		jdbcTemplate.update(
				"INSERT INTO clients (client_id, vat_number, active) VALUES (?, ?, true)",
				clientId, "TEST-VAT-9001");
		jdbcTemplate.update(
				"INSERT INTO clients_mutable (info_id, client_id, legal_name, email) VALUES (?, ?, ?, ?)",
				infoId, clientId, "Test Client", "test@example.com");
		jdbcTemplate.update(
				"INSERT INTO invoices (invoice_id, info_id, user_id, invoice_number, issue_date, due_date, vat_rate, status) "
						+ "VALUES (?, ?, ?, 1, CURRENT_DATE, CURRENT_DATE, 0.21, 'Pendiente')",
				invoiceId, infoId, UUID.randomUUID());
		jdbcTemplate.update(
				"INSERT INTO invoice_items (invoice_id, product_id, quantity, description, net_price) "
						+ "VALUES (?, 1, 2.00, 'Test item', 10.00)",
				invoiceId);

		// InvoiceSchema.items es LAZY: solo funciona si la transaccion de lectura
		// sigue abierta durante el mapeo (open-in-view = false).
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();
		assertEquals(1, invoice.getItems().size());

		// ClientMutableSchema.client tambien es LAZY.
		List<Client> clients = clientRepository.getClientsPaginated(0, 10).orElseThrow();
		assertFalse(clients.isEmpty());
	}
}
