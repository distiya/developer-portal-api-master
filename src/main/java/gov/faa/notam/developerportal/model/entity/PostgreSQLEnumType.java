package gov.faa.notam.developerportal.model.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

/**
 * Maps Java enum types to Postgres enum.
 */
public class PostgreSQLEnumType extends EnumType {
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        st.setObject(index, value != null ? value.toString() : null, Types.OTHER);
    }
}
