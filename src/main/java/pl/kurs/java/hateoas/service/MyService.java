package pl.kurs.java.hateoas.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyService {
	
	private final EntityManager entityManager;

	public List<String> exportColumns(Class<?> entity, String[] columns) {
		List<Object[]> select = selectColumnsFromTable(entity, columns);	
		List<String> selectData = new ArrayList<String>();
		
		if (columns.length > 1) {
			for (Object[] objectArray : select) {
				String line = "";
				for (Object o : objectArray) {
					line += o.toString() + ",";
				}
				selectData.add(line.substring(0, line.length()-1));
			}
		}
		else {
			for (Object o : select) {
				selectData.add(o.toString());
			}
		}
		
		return selectData;
	}
	
	public List<Object[]> selectColumnsFromTable(Class<?> entity, String[] columns) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Object[]> criteria = builder.createQuery( Object[].class );
		Root<?> root = criteria.from(entity);
				
		List<Selection<?>> selections = new ArrayList<Selection<?>>();
		for(String col : columns) {
			selections.add(root.get(col));
		}
		criteria.select(builder.array(selections.toArray(new Selection<?>[selections.size()])));
		
		return entityManager.createQuery(criteria).getResultList();
	}
	
}
