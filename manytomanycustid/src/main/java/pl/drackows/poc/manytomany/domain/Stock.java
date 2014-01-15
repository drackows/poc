package pl.drackows.poc.manytomany.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "stock") 
public class Stock implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "STOCK_ID", nullable = false)
	private Integer stockId;

	@Column(name = "STOCK_CODE", unique = true, nullable = false, length = 10)
	private String stockCode;
	
	@Column(name = "STOCK_NAME", unique = true, nullable = false, length = 20)
	private String stockName;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "stock_category", 
		joinColumns = { 
			@JoinColumn(name = "STOCK_ID", nullable = false, updatable = false) 
		}, 
		inverseJoinColumns = { 
			@JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) 
		}
	)
	private Set<Category> categories = new HashSet<Category>(0);

	public Stock() {
	}

	public Stock(String stockCode, String stockName) {
		this.stockCode = stockCode;
		this.stockName = stockName;
	}

	public Stock(String stockCode, String stockName, Set<Category> categories) {
		this.stockCode = stockCode;
		this.stockName = stockName;
		this.categories = categories;
	}

	public Integer getStockId() {
		return this.stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getStockCode() {
		return this.stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Set<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
    public String toString() {
		boolean writeCategories = true; 
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement e : stackTrace) {
			if (e.getClassName().equals(Category.class.getName()) && e.getMethodName().equals("toString")) {
				writeCategories = false;
			}
		}
	    return "Stock [stockId=" + stockId + ", stockCode=" + stockCode + ", stockName=" + stockName + ", \ncategories=" + (writeCategories?categories:"[...]") + "\n]\n";
    }

}