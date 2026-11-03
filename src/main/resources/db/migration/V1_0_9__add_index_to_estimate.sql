CREATE INDEX idx_estimate_status
    ON asr.estimate (status);

CREATE INDEX idx_estimate_company_name
    ON asr.estimate (company_name);
