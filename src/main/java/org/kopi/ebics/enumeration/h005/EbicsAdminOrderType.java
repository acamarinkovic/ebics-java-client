package org.kopi.ebics.enumeration.h005;

public enum EbicsAdminOrderType {
    HIA, HAA, HKD, HPB, HPD, HTD, INI, SPR, HCA, HAC, HCS, HEV,
    HVD, HVE, HVS, HVT, HVU, HVZ, H3K, PTK, PUB,
    FUL,  //EBICS 2.4/2.5 FR Upload (standard business order types)
    FDL,  //EBICS 2.4/2.5 FR Download (standard business order types)
    UPL,  //EBICS 2.4/2.5 DE Upload (standard business order types)
    DNL,  //EBICS 2.4/2.5 DE Download (standard business order types)
    BTU,  //EBICS 3.0 Upload
    BTD //EBICS 3.0 Download
}
