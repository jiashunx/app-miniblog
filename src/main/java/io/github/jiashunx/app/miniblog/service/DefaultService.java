package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;
import io.github.jiashunx.masker.rest.framework.function.VoidFunc;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;
import io.github.jiashunx.tools.sqlite3.exception.SQLite3MappingException;
import io.github.jiashunx.tools.sqlite3.model.TableModel;
import io.github.jiashunx.tools.sqlite3.util.SQLite3Utils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jiashunx
 */
public abstract class DefaultService<Entity> {

    private final SQLite3JdbcTemplate jdbcTemplate;

    private final Entity defaultEntity;
    private volatile boolean listAllMethodInvoked = false;
    private final Map<String, Entity> entityMap = new HashMap<>();
    private final ReentrantReadWriteLock entityMapLock = new ReentrantReadWriteLock();

    public DefaultService(SQLite3JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate);
        try {
            this.defaultEntity = getEntityClass().newInstance();
        } catch (Throwable throwable) {
            throw new MiniBlogException(String.format("create entity [%s] instance failed", getEntityClass()), throwable);
        }
    }

    public SQLite3JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private synchronized void setListAllMethodInvoked(boolean listAllMethodInvoked) {
        this.listAllMethodInvoked = listAllMethodInvoked;
    }

    protected abstract Class<Entity> getEntityClass();

    protected void entityMapReadLock(VoidFunc voidFunc) {
        entityMapLock.readLock().lock();
        try {
            voidFunc.doSomething();
        } finally {
            entityMapLock.readLock().unlock();
        }
    }

    protected void entityMapWriteLock(VoidFunc voidFunc) {
        entityMapLock.writeLock().lock();
        try {
            voidFunc.doSomething();
        } finally {
            entityMapLock.writeLock().unlock();
        }
    }

    public List<Entity> listAll() throws NullPointerException, SQLite3MappingException {
        AtomicReference<List<Entity>> ref = new AtomicReference<>();
        if (!listAllMethodInvoked) {
            entityMapWriteLock(() -> {
                TableModel tableModel = SQLite3Utils.getClassTableModel(getEntityClass());
                String tableName = tableModel.getTableName();
                ref.set(getJdbcTemplate().queryForList("SELECT * FROM " + tableName, getEntityClass()));
                setListAllMethodInvoked(true);
            });
        }
        entityMapReadLock(() -> {
            ref.set(new ArrayList<>(entityMap.values()));
        });
        List<Entity> entityList = ref.get();
        return entityList;
    }

    public Entity find(String id) throws NullPointerException, SQLite3MappingException {
        if (id == null) {
            throw new NullPointerException();
        }
        AtomicReference<Entity> ref = new AtomicReference<>();
        entityMapReadLock(() -> {
            ref.set(entityMap.get(id));
        });
        if (ref.get() == null) {
            entityMapWriteLock(() -> {
                TableModel tableModel = SQLite3Utils.getClassTableModel(getEntityClass());
                String tableName = tableModel.getTableName();
                String idColumnName = tableModel.getIdColumnModel().getColumnName();
                Entity tmpEntity = getJdbcTemplate().queryForObj(
                        String.format("SELECT * FROM %s WHERE %s=?", tableName, idColumnName), statement -> {
                            statement.setString(1, id);
                        }, getEntityClass());
                if (tmpEntity == null) {
                    tmpEntity = defaultEntity;
                }
                entityMap.put(id, tmpEntity);
                ref.set(tmpEntity);
            });
        }
        Entity entity = ref.get();
        if (entity == defaultEntity) {
            entity = null;
        }
        return entity;
    }

    public Entity insert(Entity entity) throws NullPointerException, SQLite3MappingException {
        if (entity == null) {
            throw new NullPointerException();
        }
        entityMapWriteLock(() -> {
            String id = getEntityId(entity);
            jdbcTemplate.insert(entity);
            entityMap.put(id, entity);
        });
        return entity;
    }

    private String getEntityId(Entity entity) throws NullPointerException, SQLite3MappingException {
        TableModel tableModel = SQLite3Utils.getClassTableModel(getEntityClass());
        String id = null;
        try {
            Field idField = tableModel.getIdColumnModel().getField();
            idField.setAccessible(true);
            id = String.valueOf(idField.get(entity));
        } catch (Throwable throwable) {
            throw new SQLite3MappingException();
        }
        return id;
    }

    public List<Entity> insert(List<Entity> entities) throws NullPointerException, SQLite3MappingException {
        if (entities == null) {
            throw new NullPointerException();
        }
        entities.forEach(entity -> {
            if (entity == null) {
                throw new NullPointerException();
            }
        });
        entityMapWriteLock(() -> {
            Map<String, Entity> map = new HashMap<>();
            entities.forEach(entity -> {
                map.put(getEntityId(entity), entity);
            });
            jdbcTemplate.insert(entities);
            entityMap.putAll(map);
        });
        return entities;
    }

    public Entity update(Entity entity) throws NullPointerException, SQLite3MappingException {
        if (entity == null) {
            throw new NullPointerException();
        }
        entityMapWriteLock(() -> {
            String id = getEntityId(entity);
            jdbcTemplate.update(entity);
            entityMap.put(id, entity);
        });
        return entity;
    }

    public List<Entity> update(List<Entity> entities) throws NullPointerException, SQLite3MappingException {
        if (entities == null) {
            throw new NullPointerException();
        }
        entities.forEach(entity -> {
            if (entity == null) {
                throw new NullPointerException();
            }
        });
        entityMapWriteLock(() -> {
            Map<String, Entity> map = new HashMap<>();
            entities.forEach(entity -> {
                map.put(getEntityId(entity), entity);
            });
            jdbcTemplate.update(entities);
            entityMap.putAll(map);
        });
        return entities;
    }

    public int delete(Entity entity) throws NullPointerException, SQLite3MappingException {
        return delete(Collections.singletonList(entity));
    }

    public int delete(List<Entity> entities) throws NullPointerException, SQLite3MappingException {
        if (entities == null) {
            throw new NullPointerException();
        }
        List<String> idList = new ArrayList<>(entities.size());
        entities.forEach(entity -> {
            if (entity == null) {
                throw new NullPointerException();
            }
            idList.add(getEntityId(entity));
        });
        return deleteById(idList);
    }

    public int deleteById(String id) throws NullPointerException, SQLite3MappingException {
        return deleteById(Collections.singletonList(id));
    }

    public int deleteById(List<String> idList) throws NullPointerException, SQLite3MappingException {
        if (idList == null) {
            throw new NullPointerException();
        }
        idList.forEach(entity -> {
            if (entity == null) {
                throw new NullPointerException();
            }
        });
        AtomicReference<Integer> ref = new AtomicReference<>();
        entityMapWriteLock(() -> {
            TableModel tableModel = SQLite3Utils.getClassTableModel(getEntityClass());
            String tableName = tableModel.getTableName();
            String idColumnName = tableModel.getIdColumnModel().getColumnName();
            ref.set(jdbcTemplate.batchUpdate(String.format("DELETE FROM %s WHERE %s=?", tableName, idColumnName), idList.size(), (index, statement) -> {
                statement.setString(1, idList.get(index));
            }));
            idList.forEach(entityMap::remove);
        });
        return ref.get();
    }

}
