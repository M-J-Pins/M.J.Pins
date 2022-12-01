from sqlalchemy import Column, delete, select
from sqlalchemy.dialects.postgresql import TIMESTAMP, UUID
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql import func

from quick_wallet.database import DeclarativeBase


# It's a class that inherits from the DeclarativeBase class, which is a class that inherits from the
# SQLAlchemy Base class
class BaseTable(DeclarativeBase):
    __abstract__ = True

    id = Column(
        UUID(as_uuid=True),
        primary_key=True,
        server_default=func.gen_random_uuid(),
        unique=True,
        doc="Unique index of element (type UUID)",
    )
    date_created = Column(
        TIMESTAMP(timezone=True),
        server_default=func.current_timestamp(),
        nullable=False,
        doc="Date and time of create (type TIMESTAMP)",
    )
    date_updated = Column(
        TIMESTAMP(timezone=True),
        server_default=func.current_timestamp(),
        onupdate=func.current_timestamp(),
        nullable=False,
        doc="Date and time of last update (type TIMESTAMP)",
    )

    def __repr__(self):
        columns = {
            column.name: getattr(self, column.name) for column in self.__table__.columns
        }
        return f'<{self.__tablename__}: {", ".join(map(lambda x: f"{x[0]}={x[1]}", columns.items()))}>'

    @classmethod
    async def create(cls, db: AsyncSession, **kwargs):
        """
        It creates a new object, adds it to the database, commits the changes, and then refreshes the
        object so that it has the correct id

        :param cls: The class of the object to be created
        :param db: AsyncSession - the database session
        :type db: AsyncSession
        :return: The new_object is being returned.
        """
        new_object = cls(**kwargs)
        db.add(new_object)
        await db.commit()
        await db.refresh(new_object)
        return new_object

    @classmethod
    async def get(cls, db: AsyncSession, **kwargs):
        """
        It takes a class, a database session, and a dictionary of keyword arguments, and returns the
        first row in the database that matches the keyword arguments

        :param cls: The class that is being queried
        :param db: AsyncSession - this is the database session that we will use to execute the query
        :type db: AsyncSession
        :return: The result of the query.
        """
        query = select(cls).filter_by(**kwargs).limit(1)
        result = await db.execute(query)
        return result.scalar()

    @classmethod
    async def get_all(cls, db: AsyncSession, limit: int = None, **kwargs):
        """
        It takes a class, a database session, a limit, and a bunch of keyword arguments, and returns a
        list of all the objects in the database that match the keyword arguments

        :param cls: The class of the model you're querying
        :param db: AsyncSession - the database session
        :type db: AsyncSession
        :param limit: int = None
        :type limit: int
        :return: A list of objects.
        """
        if limit is None:
            query = select(cls).filter_by(**kwargs)
        else:
            query = select(cls).filter_by(**kwargs).limit(limit)
        result = await db.execute(query)
        return result.scalars().all()

    @classmethod
    async def delete(cls, db: AsyncSession, **kwargs):
        query = delete(cls).filter_by(**kwargs)
        await db.execute(query)
        await db.commit()
