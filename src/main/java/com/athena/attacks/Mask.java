package com.athena.attacks;

import com.athena.utils.CounterList;
import com.athena.utils.HashManager;
import com.athena.utils.StringUtils;
import com.athena.utils.enums.CharSet;

import java.util.*;

public class Mask extends Attack {
    private CounterList<Byte> candidateElements;
    private String mask;

    public Mask(String mask, String hashes_filename, int hashType) {
        super.setHashType(hashType, hashes_filename);
        super.setHashman(new HashManager(hashes_filename));

        this.mask = mask;
        this.candidateElements = new CounterList<>();
        parseMask(mask);
    }

    @Override
    public void attack() {
        for (int i = 0; i < candidateElements.size(); i++) {
            if (!super.isAllCracked()) {
                super.checkAttempt(StringUtils.listToByteArray(candidateElements.get(i)));
            } else {
                return;
            }
        }
    }

    @Override
    public ArrayList<byte[]> getNextCandidates() {
        return null;
    }

    private void parseMask(String mask) {
        boolean modifier = false;

        for (byte b : mask.getBytes()) {
            switch (b) {
                case 63:
                    modifier = true;
                default:
                    if (modifier) {
                        switch (b) {
                            case 108:
                                candidateElements.add(CharSet.LOWER_ALPHABETIC.getCharsList());
                                modifier = false;
                                break;
                            case 100:
                                candidateElements.add(CharSet.NUMERIC.getCharsList());
                                modifier = false;
                                break;
                            case 115:
                                candidateElements.add(CharSet.SPECIAL.getCharsList());
                                modifier = false;
                                break;
                            case 117:
                                candidateElements.add(CharSet.UPPER_ALPHABETIC.getCharsList());
                                modifier = false;
                                break;
                            default:
                                break;
                        }
                    } else {
                        candidateElements.add(Collections.singletonList(b));
                    }
            }
        }
    }

    public CounterList<Byte> getCandidateElements() {
        return candidateElements;
    }
}
