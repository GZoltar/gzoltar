package com.gzoltar.sfl;

import com.gzoltar.sfl.formulas.Anderberg;
import com.gzoltar.sfl.formulas.Barinel;
import com.gzoltar.sfl.formulas.DStar;
import com.gzoltar.sfl.formulas.ISFLFormula;
import com.gzoltar.sfl.formulas.Ideal;
import com.gzoltar.sfl.formulas.Jaccard;
import com.gzoltar.sfl.formulas.Kulczynski2;
import com.gzoltar.sfl.formulas.Naish1;
import com.gzoltar.sfl.formulas.Ochiai;
import com.gzoltar.sfl.formulas.Ochiai2;
import com.gzoltar.sfl.formulas.Opt;
import com.gzoltar.sfl.formulas.RogersTanimoto;
import com.gzoltar.sfl.formulas.RusselRao;
import com.gzoltar.sfl.formulas.SBI;
import com.gzoltar.sfl.formulas.SimpleMatching;
import com.gzoltar.sfl.formulas.SorensenDice;
import com.gzoltar.sfl.formulas.Tarantula;

public enum SFLFormulas {

  OCHIAI(new Ochiai()),

  OCHIAI2(new Ochiai2()),

  TARANTULA(new Tarantula()),

  JACCARD(new Jaccard()),

  SBI(new SBI()),

  KULCZYNSKI2(new Kulczynski2()),

  SORENSEN_DICE(new SorensenDice()),

  ANDERBERG(new Anderberg()),

  SIMPLE_MATCHING(new SimpleMatching()),

  ROGERS_TANIMOTO(new RogersTanimoto()),

  RUSSEL_RAO(new RusselRao()),

  DSTAR(new DStar()),

  OPT(new Opt()),

  BARINEL(new Barinel()),

  IDEAL(new Ideal()),

  NAISH1(new Naish1());

  private final ISFLFormula formula;

  private SFLFormulas(final ISFLFormula formula) {
    this.formula = formula;
  }

  public ISFLFormula getFormula() {
    return this.formula;
  }
}
