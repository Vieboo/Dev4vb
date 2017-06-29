package vieboo.orm.lib;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13 0013.
 * T：bean类型
 * V：主键id的类型
 */
public interface IDao<T, V> {

    public abstract T getSingleById(V id);

    public abstract List<T> getAll();

    public abstract boolean update(T t);

    public abstract boolean deleteById(V id);

    public abstract int deleteByIds(Collection<V> ids);

    public abstract boolean delete(T t);

    public abstract boolean add(T t);

    public abstract int addAll(Collection<T> list);

    public abstract boolean addOrUpdate(T t);

    public int updateBySQL(String statement, String... arguments);

    public List<T> getListByFieldAndOrderBy(Map<String, Object> fieldValues,
                                            Map<String, Boolean> orderBy);

    public abstract boolean isExist(V id);

}
