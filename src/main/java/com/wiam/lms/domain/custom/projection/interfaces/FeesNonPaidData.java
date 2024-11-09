package com.wiam.lms.domain.custom.projection.interfaces;

public interface FeesNonPaidData {
    public String getFirstName();

    public String getLastName();
    public int getLastPaidMonth();
    public int getMissingPaidMonth();
}
