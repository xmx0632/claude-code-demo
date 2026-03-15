# Database Documentation

This directory contains comprehensive documentation for the TodoList application database.

## Documents

### 1. [migrations.md](migrations.md)
Complete database migration documentation including:
- Overview and migration strategy
- Detailed history of all migrations
- Step-by-step migration descriptions
- Rollback procedures
- Verification queries
- Troubleshooting guide

**When to use**:
- Understanding the database evolution
- Rolling back migrations
- Troubleshooting migration issues
- Learning about schema changes

### 2. [validation-queries.sql](validation-queries.sql)
Comprehensive SQL script to validate database state after migrations.

**When to use**:
- After running migrations to verify success
- Troubleshooting data integrity issues
- Auditing database configuration
- Generating reports on database state

**How to run**:
```bash
mysql -u root -p todolist < validation-queries.sql
```

Or within MySQL client:
```sql
USE todolist;
source /path/to/validation-queries.sql;
```

## Quick Reference

### Database Connection

```yaml
Database: todolist
Host: localhost
Port: 3306
Charset: utf8mb4
Collation: utf8mb4_unicode_ci
Timezone: UTC
```

### Migration Scripts Location

```
src/main/resources/db/migration/
```

### Tables Overview

| Table | Purpose | Rows (after V6) |
|-------|---------|-----------------|
| sys_user | User accounts | 1 |
| sys_todo | Todo items | 10 |
| sys_category | Categories/tags | 3 |
| sys_todo_category | Todo-category relationships | 13 |

### Default Credentials

```
Username: admin
Password: admin123
```

**⚠️ IMPORTANT**: Change the default password after first login!

## Common Tasks

### Run All Migrations

Using Flyway CLI:
```bash
flyway migrate
```

Using Spring Boot:
```bash
mvn spring-boot:run
```

Migrations will run automatically on application startup.

### Validate Migration Status

```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

All migrations should show `success = 1`.

### Check Table Structure

```sql
DESCRIBE sys_user;
DESCRIBE sys_todo;
DESCRIBE sys_category;
DESCRIBE sys_todo_category;
```

### Verify Data Integrity

Run the validation script:
```bash
mysql -u root -p todolist < docs/database/validation-queries.sql
```

## Related Documentation

- [Database Design](../detailed-design/database-design.md) - Complete schema design documentation
- [API Specifications](../detailed-design/api-specs.md) - REST API endpoints
- [Data Models](../detailed-design/data-models.md) - Application data models
- [Architecture](../architecture/architecture.md) - System architecture overview

## Support

For issues or questions related to the database:
1. Check [migrations.md](migrations.md) troubleshooting section
2. Run validation queries to diagnose problems
3. Review database design documentation
4. Contact the Database Administrator

---

**Last Updated**: 2026-01-26
**Maintained By**: Database Administrator
