package com.vb.orm.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *  ORMLite 注解的含义
 *  @DatabaseTable(columnName = "name" ）       //列名
 *  @DatabaseTable(tableName = "person" ）      //设置表的名字
 *  @DatabaseField(generatedId = true)              //generatedId    自增涨   不一定是id
 *  @DatabaseField(canBeNull = true, defaultValue = "name")   // canBeNull 表示是否可以为空     defaultValue 默认值
 *  @DatabaseField(id = true)     // id  表示主键
 *
 *  一对一
 * @DatabaseField(foreign=true,foreignAutoRefresh=true)         //foreign 表示外键    foreignAutoRefresh 表示 自动查询（当查询该对象的时候会自动查询他的外键对象）
 *
 * 一对多  必须两方是都添加相应的属性
 *
 * 一的一方
 * @ForeignCollectionField
 * private ForeignCollection<User> users;     //必须用ForignCollection  或者 Collection
 * 在一方的数据库里面不会产生该字段  ,如果产生一个存放多方的字段，你想他怎么存
 * 查找的时候去 拿着 的一方的“主键”值去多的一方数据库里面将所有数据符合条件的数据都查
 * @ForeignCollectionField(eager = true)  //eager  表示依赖加载       就是当查询该对象的时候 也会立即查询出他所带的  集合
 * 多的一方
 * @DatabaseField(foreign=true,foreignAutoRefresh=true)
 *
 * Created by Vieboo on 2016/4/19.
 */
public abstract class AbstractDataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "AbstractDataBaseHelper";

    private List<Class> tableBeans;
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    public AbstractDataBaseHelper(Context context, String dbName, int dbVersion, List<Class> beans) {
        super(context, dbName, null, dbVersion);
        this.tableBeans = beans;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.i(TAG, "ORMLite数据库创建----->onCreate");
        createTable(connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        Log.i(TAG, "ORMLite数据库更新----->onUpgrade");
        dropTable(connectionSource);
        //重新创建新版的表
        createTable(connectionSource);
    }

    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
        daos.clear();
    }

    /**
     * 创建所有的表
     * @param connectionSource
     */
    protected void createTable(ConnectionSource connectionSource) {
        if(null == tableBeans) return ;
        try {
            //这里创建表
            for(Class clazz : tableBeans) {
                TableUtils.createTableIfNotExists(connectionSource, clazz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除所有的表
     * @param connectionSource
     */
    protected void dropTable(ConnectionSource connectionSource) {
        if(null == tableBeans) return ;
        try {
            //删除旧的数据库表
            for(Class clazz : tableBeans) {
                TableUtils.dropTable(connectionSource, clazz, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每一个数据库中的表，要有一个获得Dao的方法。 可以使用一种更通用的模板方法如：
     *
     * public Dao<Class, Integer> getORMLiteDao(Class cls) throws SQLException {
     * if (dao == null) { dao = getDao(cls); }
     *
     * return dao; }
     */
    public synchronized Dao getDao(Class clazz) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Dao dao = null;
        String className = clazz.getSimpleName();
        if(daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            try {
                dao = super.getDao(clazz);
                daos.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }
}
