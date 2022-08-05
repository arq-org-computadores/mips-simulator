package br.ufrpe.mips.simulator.utils.register;

import java.util.Map;

/**
 * Classe utilitário responsável pelo mapeamento
 * do número de registradores para labels e
 * vice-versa.
 * 
 * 
 * @version 1.0
 */
public final class RegisterMapper {

    private static final Map<String, Integer> labelToNum;

    static {
        labelToNum = Map.ofEntries(
                Map.entry("zero", 0),
                Map.entry("at", 1),
                Map.entry("v0", 2),
                Map.entry("v1", 3),
                Map.entry("a0", 4),
                Map.entry("a1", 5),
                Map.entry("a2", 6),
                Map.entry("a3", 7),
                Map.entry("t0", 8),
                Map.entry("t1", 9),
                Map.entry("t2", 10),
                Map.entry("t3", 11),
                Map.entry("t4", 12),
                Map.entry("t5", 13),
                Map.entry("t6", 14),
                Map.entry("t7", 15),
                Map.entry("s0", 16),
                Map.entry("s1", 17),
                Map.entry("s2", 18),
                Map.entry("s3", 19),
                Map.entry("s4", 20),
                Map.entry("s5", 21),
                Map.entry("s6", 22),
                Map.entry("s7", 23),
                Map.entry("t8", 24),
                Map.entry("t9", 25),
                Map.entry("k0", 26),
                Map.entry("k1", 27),
                Map.entry("gp", 28),
                Map.entry("sp", 29),
                Map.entry("fp", 30),
                Map.entry("ra", 31));
    }

    /**
     * Retorna o número de um registrador dado sua label.
     * 
     * @param label String.
     * @return número do registrador ou -1 (caso não exista).
     */
    public static Integer regNumberFromLabel(String label) {
        if (!labelToNum.containsKey(label)) {
            Integer v = null;

            try {
                v = Integer.parseInt(label);
            } catch (NumberFormatException e) {
                return -1;
            }

            return v;
        }

        return labelToNum.get(label);
    }

    /**
     * Retorna a label de um registrador dado seu número.
     * 
     * @param regNumber inteiro.
     * @return label do registrador ou "" (caso não exista).
     */
    public static String labelFromRegNumber(Integer regNumber) {
        return labelToNum.entrySet().stream().filter(e -> e.getValue() == regNumber).findFirst()
                .orElse(Map.entry("", -1)).getKey();
    }

    private RegisterMapper() {

    }
}
