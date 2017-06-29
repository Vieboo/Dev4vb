package vieboo.orm.lib;

import android.database.SQLException;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13 0013.
 * T：bean类型
 * V：主键id的类型
 */
public abstract class AbstractDao<T, V> implements IDao<T, V> {

    public abstract String getTableName();

    public Dao dao;

    public AbstractDao(Dao dao) {
        this.dao = dao;
    }

    @Override
    public T getSingleById(V id) {
        if (dao == null)
            return null;
        try {
            return (T) dao.queryForId(id);
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> getListByFieldAndOrderBy(Map<String, Object> fieldValues,
                                            Map<String, Boolean> orderBy) {
        if (dao == null)
            return null;
        try {
            QueryBuilder<T, Integer> qb = dao.queryBuilder();
            if (orderBy != null) {
                for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                    qb.orderBy(entry.getKey(), entry.getValue());
                }
            }
            if (fieldValues != null) {
                Where<T, Integer> where = qb.where();
                for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                    where.eq(entry.getKey(), entry.getValue());
                }
            }
            return qb.query();

            // return dao.queryForFieldValuesArgs(fieldValues);
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        if (dao == null)
            return null;
        try {
            return dao.queryForAll();
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> getAllOrderBy(String columnName, boolean ascending) {
        if (dao == null)
            return null;
        try {
            return dao.queryBuilder().orderBy(columnName, ascending).query();
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(T t) {
        if (dao == null)
            return false;
        try {
            int update = dao.update(t);
            Log.d("ormlite", "update="+update);
            return update == 1;
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int updateBySQL(String statement, String... arguments) {
        if (dao == null)
            return 0;
        try {
            return dao.updateRaw(statement, arguments);
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean deleteById(V id) {
        if (dao == null)
            return false;
        try {
           int delete = dao.deleteById(id);
            return delete == 1;
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int deleteByIds(Collection<V> ids) {
        if (dao == null)
            return 0;
        try {
            return dao.deleteIds(ids);
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public boolean deleteAll() {
        String tableName = getTableName();
        if (dao == null)
            return false;
        try {
            int raw = dao.executeRaw("DELETE FROM " + tableName);  //返回成功删除的个数
            Log.d("ormlite", "deleteAll="+raw);
            return raw > 0;
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(T t) {
        if (dao == null)
            return false;
        try {
            int delete = dao.delete(t);
            Log.d("ormlite", "delete="+delete);
            return delete == 1;
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(T t) {
        if (dao == null)
            return false;
        try {
            int b = dao.create(t);  //成功返回1
            Log.d("ormlite", "add="+b);
            return b==1;
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int addAll(Collection<T> list) {
        if (dao == null)
            return 0;
        try {
            return dao.create(list);
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 存在就更新，不存在就添加
     * @param t
     * @return
     */
    @Override
    public boolean addOrUpdate(T t) {
        if (dao == null)
            return false;
        try {
            dao.createOrUpdate(t);
            return true;
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isExist(V id) {
        if (dao == null)
            return false;
        try {
            return dao.idExists(id);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
