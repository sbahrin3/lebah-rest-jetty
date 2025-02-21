package lebah.db.entity;

public class Transaction {

	Persistence db = null;

	public Transaction() throws Exception {
		db = Persistence.db();
	}

	public Persistence db() {
		return db;
	}

	public void begin() {
		db.beginTransaction();
	}

	public void commit() {
		db.commitTransaction();
	}

	public void rollback() {
		db.rollbackTransaction();
	}

	public void save(Object object) throws Exception {
		db.saveOnCommit(object);
	}

	public void save(Object[] objects) throws Exception {
		db.saveOnCommit(objects);
	}

	public void update(Object object) throws Exception {
		db.updateOnCommit(object);
	}

	public void update(Object[] objects) throws Exception {
		db.updateOnCommit(objects);
	}

	public void delete(Object object) throws Exception {
		db.deleteOnCommit(object);
	}

	public void delete(Object[] objects) throws Exception {
		db.deleteOnCommit(objects);
	}
}
