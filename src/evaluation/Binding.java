package evaluation;

import java.util.HashMap;
import java.util.Map;

public class Binding {

    /*
     * Mappa:
     *
     * variabile -> valore
     *
     * Esempio AnyBURL:
     *
     * X -> /m/123
     * Y -> /m/456
     *
     * Esempio AMIE3:
     *
     * ?e -> /m/123
     * ?a -> /m/456
     */
    private final Map<String, String> bindings;


    /*
     * ==========================================
     * COSTRUTTORE VUOTO
     * ==========================================
     */

    public Binding() {

        bindings =
                new HashMap<>();
    }


    /*
     * ==========================================
     * COSTRUTTORE DI COPIA
     * ==========================================
     *
     * Serve eventualmente per creare una copia
     * indipendente di un Binding.
     */

    public Binding(Binding other) {

        bindings =
                new HashMap<>(
                        other.bindings
                );
    }


    /*
     * ==========================================
     * INSERIMENTO
     * ==========================================
     */

    public void put(
            String variable,
            String value) {

        bindings.put(
                variable,
                value
        );
    }


    /*
     * ==========================================
     * RECUPERO
     * ==========================================
     */

    public String get(
            String variable) {

        return bindings.get(
                variable
        );
    }


    /*
     * ==========================================
     * CONTROLLO PRESENZA
     * ==========================================
     */

    public boolean contains(
            String variable) {

        return bindings.containsKey(
                variable
        );
    }


    /*
     * ==========================================
     * RIMOZIONE
     * ==========================================
     *
     * Utilizzato dal backtracking del
     * RuleExecutor.
     */

    public void remove(
            String variable) {

        bindings.remove(
                variable
        );
    }


    /*
     * ==========================================
     * NUMERO DI BINDING
     * ==========================================
     */

    public int size() {

        return bindings.size();
    }


    /*
     * ==========================================
     * CONTROLLO VUOTO
     * ==========================================
     */

    public boolean isEmpty() {

        return bindings.isEmpty();
    }


    /*
     * ==========================================
     * SVUOTAMENTO
     * ==========================================
     */

    public void clear() {

        bindings.clear();
    }


    /*
     * ==========================================
     * STRINGA
     * ==========================================
     */

    @Override
    public String toString() {

        return bindings.toString();
    }
}