/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaactg.conforttermico;

/**
 *
 * @author Jorge
 */
public class ConfortTermico {
    // Variables de generales para calculo de PMV
    private double psy_PROP_Patm = 101325.0d;
    private double still_air_threshold = 0.1; // m/s
    private double vel_si_elevated_air_speed = 0.2; // m/s
    // Variables referentes a las condiciones actuales
    private double wMet; // wme = 00.0f; // Trabajo externo ejercido (Ejemplo: Si ahora estoy sentado = met y antes corri = wme)
    private double temAire; // ta = 25.0f; // Temperatura del Aire
    private double temRadi; // tr = 25.0f; // Temperatura Radiante
    private double velAire; // vel = 0.1f; // Velocidad del Aire
    private double humRela; // rh = 50.0f; // Humedad Relativa
    private double metActu; // met = 1.0f; // Ritmo Metabólico (Actual)
    private double cloRopa; // clo = 0.6f; // Aislamiento Térmico
    // Constructor
    public ConfortTermico (double  ta, double  vel, double  rh, double  met, double  clo) {
        // Asignar valores
        wMet = 0.0f;
        // Condicionbes 
        temAire = ta;
        temRadi = ta;
        velAire = vel;
        humRela = rh;
        metActu = met;
        cloRopa = clo;
    }
    
    public PMV calcularValorPMV () {
        // Ajustar valor de humedad relativa
        double rhFit = convert_to_tdb_rh (humRela, temAire, "rh", "rh");
        // Calcular índice o nivel de Confort Térmico
        PMV ntc = pmvElevatedAirspeed(temAire, temRadi, velAire, rhFit, metActu, cloRopa, wMet);
        // Exponer resultados
        // --System.out.println("PMV: " + ntc.pmv);
        // --System.out.println("PPD: " + ntc.ppd + " %");
        // --System.out.println("SET: " + ntc.set + " °C");
        // Regresar resultados
        return ntc;
    }
    
    private double convert_to_tdb_rh (double x, double tdb, String origin, String target) {
        double rh = x;
        double psat = satpress(tdb);
        double vappress = rh / 100 * psat;
        //double w = humratio(101325.0 /*.Patm*/, vappress);
        //double wetbulb = wetbulb(tdb, w);
        //double dewpoint = dewpoint(w);
        return rh;
    }
    
    private double satpress (double tdb) {
        double tKel = tdb + 273.15 /* TKelConv Temperatura kelvin*/,
            C1 = -5674.5359,
            C2 = 6.3925247,
            C3 = -0.9677843 * Math.pow(10, -2),
            C4 = 0.62215701 * Math.pow(10, -6),
            C5 = 0.20747825 * Math.pow(10, -8),
            C6 = -0.9484024 * Math.pow(10, -12),
            C7 = 4.1635019,
            C8 = -5800.2206,
            C9 = 1.3914993,
            C10 = -0.048640239,
            C11 = 0.41764768 * Math.pow(10, -4),
            C12 = -0.14452093 * Math.pow(10, -7),
            C13 = 6.5459673,
            pascals = 0;
        if (tKel < 273.15) {
            pascals = Math.exp(C1 / tKel + C2 + tKel * (C3 + tKel * (C4 + tKel * (C5 + C6 * tKel))) + C7 * Math.log(tKel));
        } else if (tKel >= 273.15) {
            pascals = Math.exp(C8 / tKel + C9 + tKel * (C10 + tKel * (C11 + tKel * C12)) + C13 * Math.log(tKel));
        }
        return pascals;
    }
    
    private PMV pmvElevatedAirspeed (double ta, double tr, double vel, double rh, double met, double clo, double wme) {
        // Variables
        PMV ntc = new PMV();
        double ce = 0;
        // confortmodels.js
        double set = pierceSET(ta, tr, vel, rh, met, clo, wme);
        // Umbral de aire estatico
        if (vel <= still_air_threshold) {
            ntc = pmvCalculator(ta, tr, vel, rh, met, clo, wme);
            // var ta_adj = ta
            ce = 0;
        } else {
            double ce_l = 0;
            double ce_r = 40;
            double eps = 0.001;  // precision of ce
            ce = secant(ce_l, ce_r, "anonimus_funcion", eps,
                    set, ta, tr, vel, rh, met, clo, wme);
            if ((ce == Double.NaN)) {
                ce = bisect(ce_l, ce_r, "anonimus_funcion", eps, 0,
                        set, ta, tr, vel, rh, met, clo, wme);
            }
            // Umbral de aire estatico
            ntc = pmvCalculator(ta - ce, tr - ce, still_air_threshold, rh, met, clo, wme);
        }
        // Resultados PMV
        ntc.set = set;
        ntc.ta_adj = ta - ce;
        ntc.tr_adj = tr - ce;
        ntc.cooling_effect = ce;
        // Regresar resultados
        return ntc;
    }
    
    private double bisect (double a, double b, String fn, double epsilon, double target,
            double set, double  ta, double  tr, double  vel, double  rh, double  met , double  clo, double  wme) {
        // Variables
        double a_T, b_T, midpoint = 0, midpoint_T;
        // Ciclo
        while (Math.abs(b - a) > 2 * epsilon) {
            midpoint = (b + a) / 2;
            if (fn == "anonimus_funcion") {
                a_T = anonimus_funcion(a, set, ta, tr, vel, rh, met, clo, wme);
            } else {
                a_T = 0;
            }
            if (fn == "anonimus_funcion") {
                b_T = anonimus_funcion(b, set, ta, tr, vel, rh, met, clo, wme);
            } else {
                b_T = 0;
            }
            if (fn == "anonimus_funcion") {
                midpoint_T = anonimus_funcion(midpoint, set, ta, tr, vel, rh, met, clo, wme);
            } else {
                midpoint_T = 0;
            }
            if ((a_T - target) * (midpoint_T - target) < 0)
                b = midpoint;
            else if ((b_T - target) * (midpoint_T - target) < 0)
                a = midpoint;
            else
                return -999;
        }
        return midpoint;
    }
    
    private double secant (double a, double b, String fn, double epsilon,
            double set, double  ta, double  tr, double  vel, double  rh, double  met , double  clo, double  wme) {
        // Variables
        double f1 = 0, f2 = 0;
        // root-finding only
        if (fn == "anonimus_funcion") {
            f1 = anonimus_funcion(a, set, ta, tr, vel, rh, met, clo, wme);
        }
        if (Math.abs(f1) <= epsilon) return a;
        if (fn == "anonimus_funcion") {
            f2 = anonimus_funcion(b, set, ta, tr, vel, rh, met, clo, wme);
        }
        if (Math.abs(f2) <= epsilon) {
            return b;
        }
        // Variables
        double slope, c, f3 = 0;
        // Ciclo
        for (int i = 0; i < 100; i++){
            slope = (f2 - f1) / (b - a);
            c = b - f2/slope;
            if (fn == "anonimus_funcion") {
                f3 = anonimus_funcion(c, set, ta, tr, vel, rh, met, clo, wme);
            }
            if (Math.abs(f3) < epsilon) return c;
            a = b;
            b = c;
            f1 = f2;
            f2 = f3;
        }
        // Regresar resultado
        return Double.NaN;
    }
    
    private double anonimus_funcion (double ce, double set, double  ta, double  tr, double  vel, double  rh, double  met , double  clo, double  wme){
        // Calcular y regresar resultado
        return (set - pierceSET(ta - ce, tr - ce, still_air_threshold, rh, met, clo, wme));
    }
    
    private PMV pmvCalculator (double  ta, double  tr, double  vel, double  rh, double  met , double  clo, double  wme) { {}
        // Argumentos
        // ** ta, air temperature (°C)
        // ** tr, mean radiant temperature (°C)
        // ** vel, relative air velocity (m/s)
        // ** rh, relative humidity (%) Used only this way to input humidity level
        // ** met, metabolic rate (met)
        // ** clo, clothing (clo)
        // ** wme, external work, normally around 0 (met)
        PMV ntc = new PMV();

        double pa = 0, icl = 0, m = 0, w = 0, mw = 0, fcl = 0, hcf = 0, taa = 0, tra = 0, tcla = 0, p1 = 0, p2 = 0, p3 = 0, p4 = 0,
        p5 = 0, xn = 0, xf = 0, eps = 0, hcn = 0, hc = 0, tcl = 0, hl1 = 0, hl2 = 0, hl3 = 0, hl4 = 0, hl5 = 0, hl6 = 0,
        ts = 0, pmv = 0, ppd = 0, n = 0;

        pa = rh * 10 * Math.exp(16.6536 - 4030.183 / (ta + 235));

        icl = 0.155 * clo; //thermal insulation of the clothing in M2K/W
        m = met * 58.15; //metabolic rate in W/M2
        w = wme * 58.15; //external work in W/M2
        mw = m - w; //internal heat production in the human body
        if (icl <= 0.078) fcl = 1 + (1.29 * icl);
        else fcl = 1.05 + (0.645 * icl);

        //heat transf. coeff. by forced convection
        hcf = 12.1 * Math.sqrt(vel);
        taa = ta + 273;
        tra = tr + 273;
        tcla = taa + (35.5 - ta) / (3.5 * icl + 0.1);

        p1 = icl * fcl;
        p2 = p1 * 3.96;
        p3 = p1 * 100;
        p4 = p1 * taa;
        p5 = 308.7 - 0.028 * mw + p2 * Math.pow(tra / 100, 4);
        xn = tcla / 100;
        xf = tcla / 50;
        eps = 0.00015;

        n = 0;
        while (Math.abs(xn - xf) > eps) {
            xf = (xf + xn) / 2;
            hcn = 2.38 * Math.pow(Math.abs(100.0 * xf - taa), 0.25);
            if (hcf > hcn) hc = hcf;
            else hc = hcn;
            xn = (p5 + p4 * hc - p2 * Math.pow(xf, 4)) / (100 + p3 * hc);
            ++n;
            if (n > 150) {
                // ('Max iterations exceeded');
                ntc.pmv = 1;
                ntc.ppd = 0;
                ntc.hl1 = 0;
                ntc.hl2 = 0;
                ntc.hl3 = 0;
                ntc.hl4 = 0;
                ntc.hl5 = 0;
                ntc.hl6 = 0;

                return ntc;
            }
        }

        tcl = 100 * xn - 273;

        // heat loss diff. through skin
        hl1 = 3.05 * 0.001 * (5733 - (6.99 * mw) - pa);
        // heat loss by sweating
        if (mw > 58.15) hl2 = 0.42 * (mw - 58.15);
        else hl2 = 0;
        // latent respiration heat loss
        hl3 = 1.7 * 0.00001 * m * (5867 - pa);
        // dry respiration heat loss
        hl4 = 0.0014 * m * (34 - ta);
        // heat loss by radiation
        hl5 = 3.96 * fcl * (Math.pow(xn, 4) - Math.pow(tra / 100, 4));
        // heat loss by convection
        hl6 = fcl * hc * (tcl - ta);

        ts = 0.303 * Math.exp(-0.036 * m) + 0.028;
        pmv = ts * (mw - hl1 - hl2 - hl3 - hl4 - hl5 - hl6);
        ppd = 100.0 - 95.0 * Math.exp(-0.03353 * Math.pow(pmv, 4.0) - 0.2179 * Math.pow(pmv, 2.0));

        ntc.pmv = pmv;
        ntc.ppd = ppd;
        ntc.hl1 = hl1;
        ntc.hl2 = hl2;
        ntc.hl3 = hl3;
        ntc.hl4 = hl4;
        ntc.hl5 = hl5;
        ntc.hl6 = hl6;

        return ntc;
    }
    
    private double pierceSET(double  ta, double  tr, double  vel, double  rh, double  met , double  clo, double  wme) {
        // Linea 311
        double  VaporPressure = rh * findSaturatedVaporPressureTorr(ta) / 100;
        double  AirVelocity = Math.max(vel, 0.1d);
        double  KCLO = 0.25d;
        double  BODYWEIGHT = 69.9d;
        double  BODYSURFACEAREA = 1.8258d;
        double  METFACTOR = 58.2d;
        double  SBC = 0.000000056697d; // Stefan-Boltzmann constant (W/m2K4)
        double  CSW = 170d;
        double  CDIL = 120d;
        double  CSTR = 0.5d;
        
        double TempSkinNeutral = 33.7d; //setpoint (neutral) value for Tsk
        double TempCoreNeutral = 36.8d; //setpoint value for Tcr
        double TempBodyNeutral = 36.49d; //setpoint for Tb (.1*TempSkinNeutral + .9*TempCoreNeutral)
        double SkinBloodFlowNeutral = 6.3d; //neutral value for SkinBloodFlow
        
        //INITIAL VALUES - start of 1st experiment
        double TempSkin = TempSkinNeutral;
        double TempCore = TempCoreNeutral;
        double SkinBloodFlow = SkinBloodFlowNeutral;
        double MSHIV = 0.0d;
        double ALFA = 0.1d;
        double ESK = 0.1 * met;
        
        //Start new experiment here (for graded experiments)
        //UNIT CONVERSIONS (from input variables)

        // ¿DE DONDE SAÑE?
        double p = psy_PROP_Patm/*psy.PROP.Patm*/ / 1000; // TH : interface?

        double PressureInAtmospheres = p * 0.009869;
        double LTIME = 60.0;
        double TIMEH = LTIME / 60.0;
        double RCL = 0.155 * clo;
        // AdjustICL(RCL, Conditions);  TH: I don't think this is used in the software

        double FACL = 1.0 + 0.15 * clo; //% INCREASE IN BODY SURFACE AREA DUE TO CLOTHING
        double LR = 2.2 / PressureInAtmospheres; //Lewis Relation is 2.2 at sea level
        double RM = met * METFACTOR;
        double M = met * METFACTOR;

        double WCRIT;
        double ICL;
        if (clo <= 0) {
            WCRIT = 0.38 * Math.pow(AirVelocity, -0.29);
            ICL = 1.0;
        } else {
            WCRIT = 0.59 * Math.pow(AirVelocity, -0.08);
            ICL = 0.45;
        }

        double CHC = 3.0 * Math.pow(PressureInAtmospheres, 0.53);
        double CHCV = 8.600001 * Math.pow((AirVelocity * PressureInAtmospheres), 0.53);
        CHC = Math.max(CHC, CHCV);

        //initial estimate of Tcl
        double CHR = 4.7;
        double CTC = CHR + CHC;
        double RA = 1.0 / (FACL * CTC); //resistance of air layer to dry heat transfer
        double TOP = (CHR * tr + CHC * ta) / CTC;
        double TCL = TOP + (TempSkin - TOP) / (CTC * (RA + RCL));

        // ========================  BEGIN ITERATION
        //
        // Tcl and CHR are solved iteratively using: H(Tsk - To) = CTC(Tcl - To),
        //  where H = 1/(Ra + Rcl) and Ra = 1/Facl*CTC
        //
        
        double TCL_OLD = TCL;
        boolean flag = true;
        
        double DRY = 0;
        double HFCS = 0;
        double ERES = 0;
        double CRES = 0;
        double SCR = 0;
        double SSK = 0;
        double TCSK = 0;
        double TCCR = 0;
        double DTSK = 0;
        double DTCR = 0;
        double TB = 0;
        double SKSIG = 0;
        double WARMS = 0;
        double COLDS = 0;
        double CRSIG = 0;
        double WARMC = 0;
        double COLDC = 0;
        double BDSIG = 0;
        double WARMB = 0;
        double COLDB = 0;
        double REGSW = 0;
        double ERSW = 0;
        double REA = 0;
        double RECL = 0;
        double EMAX = 0;
        double PRSW = 0;
        double PWET = 0;
        double EDIF = 0;
        
        for (int TIM = 1; TIM <= LTIME; TIM++) {
            do {
                if (flag) {
                    TCL_OLD = TCL;
                    CHR = 4.0 * SBC * Math.pow(((TCL + tr) / 2.0 + 273.15), 3.0) * 0.72;
                    CTC = CHR + CHC;
                    RA = 1.0 / (FACL * CTC); //resistance of air layer to dry heat transfer
                    TOP = (CHR * tr + CHC * ta) / CTC;
                }
                TCL = (RA * TempSkin + RCL * TOP) / (RA + RCL);
                flag = true;
            } while (Math.abs(TCL - TCL_OLD) > 0.01);
            flag = false;
            DRY = (TempSkin - TOP) / (RA + RCL);
            HFCS = (TempCore - TempSkin) * (5.28 + 1.163 * SkinBloodFlow);
            ERES = 0.0023 * M * (44.0 - VaporPressure);
            CRES = 0.0014 * M * (34.0 - ta);
            SCR = M - HFCS - ERES - CRES - wme;
            SSK = HFCS - DRY - ESK;
            TCSK = 0.97 * ALFA * BODYWEIGHT;
            TCCR = 0.97 * (1 - ALFA) * BODYWEIGHT;
            DTSK = (SSK * BODYSURFACEAREA) / (TCSK * 60.0); //deg C per minute
            DTCR = SCR * BODYSURFACEAREA / (TCCR * 60.0); //deg C per minute
            TempSkin = TempSkin + DTSK;
            TempCore = TempCore + DTCR;
            TB = ALFA * TempSkin + (1 - ALFA) * TempCore;
            SKSIG = TempSkin - TempSkinNeutral;
            
            WARMS = 0;
            if (SKSIG > 0) {
                WARMS = 1 * SKSIG;
            }
            COLDS = 0;
            if ((-1.0 * SKSIG) > 0) {
                COLDS = 1 * (-1.0 * SKSIG);
            }
            CRSIG = (TempCore - TempCoreNeutral);
            WARMC = 0;
            if (CRSIG > 0) {
                WARMC = 1 * CRSIG;
            }
            COLDC = 0;
            if ((-1.0 * CRSIG) > 0) {
                COLDC = 1 * (-1.0 * CRSIG);
            }
            BDSIG = TB - TempBodyNeutral;
            WARMB = 0;
            if (BDSIG > 0){
                WARMB =  1 * BDSIG;
            }
            COLDB = 0;
            if ((-1.0 * BDSIG) > 0){
                COLDB = 1 * (-1.0 * BDSIG);
            }
            SkinBloodFlow = (SkinBloodFlowNeutral + CDIL * WARMC) / (1 + CSTR * COLDS);
            if (SkinBloodFlow > 90.0) SkinBloodFlow = 90.0;
            if (SkinBloodFlow < 0.5) SkinBloodFlow = 0.5;
            REGSW = CSW * WARMB * Math.exp(WARMS / 10.7);
            if (REGSW > 500.0) REGSW = 500.0;
            ERSW = 0.68 * REGSW;
            REA = 1.0 / (LR * FACL * CHC); //evaporative resistance of air layer
            RECL = RCL / (LR * ICL); //evaporative resistance of clothing (icl=.45)
            EMAX = (findSaturatedVaporPressureTorr(TempSkin) - VaporPressure) / (REA + RECL);
            PRSW = ERSW / EMAX;
            PWET = 0.06 + 0.94 * PRSW;
            EDIF = PWET * EMAX - ERSW;
            ESK = ERSW + EDIF;
            if (PWET > WCRIT) {
                PWET = WCRIT;
                PRSW = WCRIT / 0.94;
                ERSW = PRSW * EMAX;
                EDIF = 0.06 * (1.0 - PRSW) * EMAX;
                ESK = ERSW + EDIF;
            }
            if (EMAX < 0) {
                EDIF = 0;
                ERSW = 0;
                PWET = WCRIT;
                PRSW = WCRIT;
                ESK = EMAX;
            }
            ESK = ERSW + EDIF;
            MSHIV = 19.4 * COLDS * COLDC;
            M = RM + MSHIV;
            ALFA = 0.0417737 + 0.7451833 / (SkinBloodFlow + .585417);
        }

        //Define new heat flow terms, coeffs, and abbreviations
        double STORE = M - wme - CRES - ERES - DRY - ESK; //rate of body heat storage

        double HSK = DRY + ESK; //total heat loss from skin
        double RN = M - wme; //net metabolic heat production
        double ECOMF = 0.42 * (RN - (1 * METFACTOR));
        if (ECOMF < 0.0) ECOMF = 0.0; //from Fanger
        double EREQ = RN - ERES - CRES - DRY;
        EMAX = EMAX * WCRIT;
        double HD = 1.0 / (RA + RCL);
        double HE = 1.0 / (REA + RECL);
        double W = PWET;
        double PSSK = findSaturatedVaporPressureTorr(TempSkin);
        // Definition of ASHRAE standard environment... denoted "S"
        double CHRS = CHR;
        double CHCS = 0;
        if (met < 0.85) {
            CHCS = 3.0;
        } else {
            CHCS = 5.66 * Math.pow(((met - 0.85)), 0.39);
            if (CHCS < 3.0) CHCS = 3.0;
        }
        double CTCS = CHCS + CHRS;
        double RCLOS = 1.52 / ((met - wme / METFACTOR) + 0.6944) - 0.1835;
        double RCLS = 0.155 * RCLOS;
        double FACLS = 1.0 + KCLO * RCLOS;
        double FCLS = 1.0 / (1.0 + 0.155 * FACLS * CTCS * RCLOS);
        double IMS = 0.45;
        double ICLS = IMS * CHCS / CTCS * (1 - FCLS) / (CHCS / CTCS - FCLS * IMS);
        double RAS = 1.0 / (FACLS * CTCS);
        double REAS = 1.0 / (LR * FACLS * CHCS);
        double RECLS = RCLS / (LR * ICLS);
        double HD_S = 1.0 / (RAS + RCLS);
        double HE_S = 1.0 / (REAS + RECLS);

        // SET* (standardized humidity, clo, Pb, and CHC)
        // determined using Newton//s iterative solution
        // FNERRS is defined in the GENERAL SETUP section above

        double DELTA = .0001;
        double ERR1, ERR2;
        double  dx = 100.0;
        double X_OLD = TempSkin - HSK / HD_S; //lower bound for SET
        double X = 0;
        while (Math.abs(dx) > .01) {
            ERR1 = (HSK - HD_S * (TempSkin - X_OLD) - W * HE_S * (PSSK - 0.5 * findSaturatedVaporPressureTorr(X_OLD)));
            ERR2 = (HSK - HD_S * (TempSkin - (X_OLD + DELTA)) - W * HE_S * (PSSK - 0.5 * findSaturatedVaporPressureTorr((X_OLD + DELTA))));
            X = X_OLD - DELTA * ERR1 / (ERR2 - ERR1);
            dx = X - X_OLD;
            X_OLD = X;
        }
        return X;
    }
    
    private double findSaturatedVaporPressureTorr(double ta) {
        double  exp = 0d;
        exp = Math.exp(18.6686 - 4030.183 / (ta + 235.0));
        return exp;
    }
    
    private double max(double  vel, double  v) {
        double  r = 0d;
        return r;
    }
}
