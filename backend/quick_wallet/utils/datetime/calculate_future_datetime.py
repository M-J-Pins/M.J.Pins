from datetime import datetime, timedelta


def calculate_future_datetime(period: timedelta):
    return datetime.now() + period
