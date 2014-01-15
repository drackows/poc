package pl.drackows.poc.manytomany;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import pl.drackows.poc.manytomany.domain.Category;
import pl.drackows.poc.manytomany.domain.Stock;
import pl.drackows.poc.manytomany.util.HibernateUtil;

public class App {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Process exec = Runtime.getRuntime().exec("/home/drackowski/opt/postgres/bin/dropdb manytomany; /home/drackowski/opt/postgres/bin/createdb manytomany");
		exec.waitFor();
		
		System.out.println("Hibernate many to many (Annotation)");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		session.beginTransaction();

		Stock stock = new Stock();
		stock.setStockCode("7052");
		stock.setStockName("PADINI");

		Category category1 = new Category("CONSUMER", "CONSUMER COMPANY");
		Category category2 = new Category("INVESTMENT", "INVESTMENT COMPANY");

		Set<Category> categories = new HashSet<Category>();
		categories.add(category1);
		categories.add(category2);

		stock.setCategories(categories);

		session.save(stock);

		Stock s2 = new Stock();
		s2.setStockCode("1111");
		s2.setStockName("BLABLA");
		Set<Category> s2cats = new HashSet<Category>();
		s2cats.add(category2);
		s2.setCategories(s2cats);
		session.save(s2);

		session.getTransaction().commit();

		List<Stock> stocks = (List<Stock>) session.createCriteria(Stock.class).list();
		System.out.println(stocks);

		List<Category> cats = (List<Category>) session.createCriteria(Category.class).list();
		System.out.println(cats);

		session.getTransaction().begin();
		Stock stock_after = (Stock) session.createCriteria(Stock.class).add(Restrictions.eq("stockId", stock.getStockId())).uniqueResult();
		System.out.println(stock_after);

		Category category3 = new Category("CONSUMER2", "CONSUMER COMPANY 2");

		Set<Category> st_cat_aft = new HashSet<Category>();
		st_cat_aft.add(category3);
		st_cat_aft.add(category1);
		stock_after.setCategories(st_cat_aft);

		session.save(stock_after);

		Stock stock_after2 = (Stock) session.createCriteria(Stock.class).add(Restrictions.eq("stockId", stock.getStockId())).uniqueResult();
		System.out.println(stock_after2);

		Category ccc = (Category) session.createCriteria(Category.class).add(Restrictions.eq("categoryId", category1.getCategoryId())).uniqueResult();
		ccc.setStocks(new HashSet<Stock>(Arrays.asList(stock, s2)));
		System.out.println(ccc);

		session.getTransaction().commit();

		session.createSQLQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES");
		
		
		System.out.println("Done");
	}
}
