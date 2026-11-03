ALTER TABLE asr.estimate
    ADD COLUMN hotel_zone_data_id BIGINT;

ALTER TABLE asr.estimate
    ADD CONSTRAINT FK_ESTIMATE_ON_HOTEL_ZONE_DATA FOREIGN KEY (hotel_zone_data_id) REFERENCES asr.hotel_zone_data (id);
