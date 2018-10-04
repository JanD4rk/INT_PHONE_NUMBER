package com.voidaint.intphonenumber;

import java.util.Arrays;

public class Country {
    /**
     * Name of country
     */
    private String name;
    /**
     * ISO2 of country
     */
    private String iso2;
    /**
     * Dial code prefix of country
     */
    private int dialCode;

    /**
     * AreaCodes  of country
     */
    private String[] areaCodes;

    /**
     * Constructor
     *
     * @param name     String
     * @param iso2      String of ISO2
     * @param dialCode int
     */


    /**
     * Get name of country
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of country
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get ISO2 of country
     *
     * @return String
     */
    public String getIso2() {
        return iso2.toUpperCase();
    }

    /**
     * Set ISO2 of country
     *
     * @param iso2 String
     */
    public void setIso2(String iso2) {
        this.iso2 = iso2.toUpperCase();
    }

    /**
     * Get dial code prefix of country (like +1)
     *
     * @return int
     */
    public int getDialCode() {
        return dialCode;
    }

    /**
     * Set dial code prefix of country (like +1)
     *
     * @param dialCode int (without + prefix!)
     */
    public void setDialCode(int dialCode) {
        this.dialCode = dialCode;
    }


    /**
     * Get arecodes of country;
     *
     * @return String[]
     */
    public String[] getAreaCodes() {
        return areaCodes;
    }

    /**
     * Set arecodes of country;
     *
     * @param areaCodes String[]
     */
    public void setAreaCodes(String[] areaCodes) {
        this.areaCodes = areaCodes;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", iso2='" + iso2 + '\'' +
                ", dialCode=" + dialCode +
                ", areaCodes=" + Arrays.toString(areaCodes) +
                '}';
    }

    /**
     * Check if equals
     *
     * @param o Object to compare
     * @return boolean
     */



    @Override
    public boolean equals(Object o) {
        return (o instanceof Country) && (((Country) o).getIso2().toUpperCase().equals(this.getIso2().toUpperCase()));
    }
}
