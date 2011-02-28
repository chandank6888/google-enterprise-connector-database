package com.google.enterprise.connector.diffing;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.enterprise.connector.util.diffing.SnapshotRepository;
import com.google.enterprise.connector.util.diffing.SnapshotRepositoryRuntimeException;


/**
 * DBClassRepository Implements the @link SnapshotRepository Interface.
 * Implemented by delegating to an {@link Iterable}<{@link JsonDocument}>
 */

public class DBClassRepository implements SnapshotRepository<DBClass> {

	private static final Logger LOG = Logger.getLogger(
			DBClassRepository.class.getName());

	private final Iterable<JsonDocument> DBFetcher;

	public DBClassRepository(Iterable<JsonDocument> DBFetcher) {
		this.DBFetcher = DBFetcher;
	}

	/* @Override */
	public Iterator<DBClass> iterator() throws SnapshotRepositoryRuntimeException {
		 final Function<JsonDocument, DBClass> f = new ConversionFunction();
	    Iterator<DBClass> it1= Iterators.transform(DBFetcher.iterator(), f);
		return it1;
	}

	/* @Override */
	public String getName() {
		String result=DBClassRepository.class.getName();
		return result;
	}

	/**
	 * Class which implements function interface for transforming JsonDocument objects to 
	 * DBClass Object. 
	 */
	private static class ConversionFunction implements Function<JsonDocument, DBClass> {
		private int count=0;	   
		/* @Override */

		public DBClass apply(JsonDocument jdoc) {
			DBClass p = DBClass.factoryFunction.apply(jdoc);
			if (LOG.isLoggable(Level.FINER)) {
				LOG.finer("DBClassRepository returns person " + p.getDocumentId() + " number " + count);
			}
			count++;
			return p;
		}
	}
}
