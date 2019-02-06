package org.ledocte.owlcms.data;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_DRIVER;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_PASSWORD;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_URL;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_USER;
import static org.hibernate.cfg.AvailableSettings.QUERY_STARTUP_CHECKING;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.USE_QUERY_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER;
import static org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_STRUCTURED_CACHE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.ledocte.owlcms.data.category.Category;
import org.ledocte.owlcms.data.category.CategoryRepository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class JPAService {

	@WebListener
	public static class ContextListener implements ServletContextListener {

		@Override
		public void contextInitialized(ServletContextEvent sce) {
			init();
		}

		@Override
		public void contextDestroyed(ServletContextEvent sce) {
			close();
		}
	}

	private static EntityManagerFactory factory;

	public static void init() {
		if (factory == null) {
			// factory = Persistence.createEntityManagerFactory("test-pu");
			factory = getFactoryFromCode();
			createTestData();
		}
	}

	private static void createTestData() {
		System.out.println("Creating test org.ledocte.owlcms.data...");
		CategoryRepository.insertStandardCategories();
	}

	public static void close() {
		factory.close();
	}

	public static EntityManagerFactory getFactory() {
		return factory;
	}

	public static <T> T runInTransaction(Function<EntityManager, T> function) {
		EntityManager entityManager = null;

		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction()
				.begin();

			T result = function.apply(entityManager);

			entityManager.getTransaction()
				.commit();
			return result;

		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

	public static EntityManagerFactory getFactoryFromCode() {
		PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo(
			JPAService.class.getSimpleName());
		Map<String, Object> configuration = new HashMap<>();

		// configuration.put(AvailableSettings.INTERCEPTOR, interceptor());

		factory = new EntityManagerFactoryBuilderImpl(
				new PersistenceUnitInfoDescriptor(persistenceUnitInfo),
				configuration).build();
		return factory;
	}

	protected static PersistenceUnitInfoImpl persistenceUnitInfo(String name) {
		return new PersistenceUnitInfoImpl(name, entityClassNames(), properties());
	}

	protected static Properties properties() {
		ImmutableMap<String, Object> vals = new ImmutableMap.Builder<String, Object>()
			.put(DIALECT, H2Dialect.class.getName())
			.put(HBM2DDL_AUTO, "update")
			.put(SHOW_SQL, false)
			.put(QUERY_STARTUP_CHECKING, false)
			.put(GENERATE_STATISTICS, false)
			.put(USE_REFLECTION_OPTIMIZER, false)
			.put(USE_SECOND_LEVEL_CACHE, false)
			.put(USE_QUERY_CACHE, false)
			.put(USE_STRUCTURED_CACHE, false)
			.put(STATEMENT_BATCH_SIZE, 20)
			.build();
		Properties props = new Properties();

		props.putAll(vals);
		props.put(JPA_JDBC_URL, "jdbc:h2:mem:test");
		props.put(JPA_JDBC_DRIVER, org.h2.Driver.class.getName());
		props.put(JPA_JDBC_USER, "sa");
		props.put(JPA_JDBC_PASSWORD, "");
		props.put("javax.persistence.schema-generation.database.action", "update");
		return props;
	}

	protected static List<String> entityClassNames() {
		ImmutableList<String> vals = new ImmutableList.Builder<String>()
			.add(Category.class.getName())
			.build();
		return vals;
	}

}
