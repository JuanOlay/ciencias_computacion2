import java.util.Arrays;

public class TablaHash {
    private int[] tablaHash;
    private int numClaves;
    private int digitosClaves;
    
    public TablaHash(int numClaves, int digitosClaves) {
        this.numClaves = numClaves;
        this.digitosClaves = digitosClaves;
        this.tablaHash = new int[numClaves];
        Arrays.fill(tablaHash, -1);  // -1 indica posición vacía
    }
    
    public int getNumClaves() {
        return numClaves;
    }
    
    public int getDigitosClaves() {
        return digitosClaves;
    }
    
    public ResultadoOperacion insertarClave(int clave, String metodo) {
        // Determinar índice según método seleccionado
        int indice = transformarClave(clave, metodo);
        
        // Verificar si la posición ya está ocupada
        if (tablaHash[indice] == -1) {
            // La posición está vacía, podemos insertar
            tablaHash[indice] = clave;
            return new ResultadoOperacion(true, "Clave " + clave + " insertada en posición " + indice, indice);
        } else if (tablaHash[indice] == clave) {
            // La clave ya existe en la tabla
            return new ResultadoOperacion(false, "La clave " + clave + " ya existe en la tabla.", indice);
        } else {
            // Hay una colisión y no intentamos resolverla
            return new ResultadoOperacion(false, "Error: Colisión detectada. La posición " + indice + " ya está ocupada por la clave " + tablaHash[indice] + ". No se puede insertar la clave " + clave, indice);
        }
    }
    
    

    public ResultadoBusqueda buscarClaveHash(int clave, String metodo) {
        int indice = transformarClave(clave, metodo);
        int comparaciones = 1;
        
        if (tablaHash[indice] == clave) {
            return new ResultadoBusqueda(true, "Búsqueda por Transformación: Clave " + clave + 
                          " encontrada en posición " + indice + 
                          " (Comparaciones: " + comparaciones + ")", 
                          indice, comparaciones);
        }
        
        return new ResultadoBusqueda(false, "Búsqueda por Transformación: Clave " + clave + 
                       " no encontrada en la posición " + indice + 
                       " (Comparaciones: " + comparaciones + ")", 
                       -1, comparaciones);
    }
    
    public ResultadoBusqueda buscarClaveLineal(int clave) {
        int comparaciones = 0;
        
        for (int i = 0; i < numClaves; i++) {
            comparaciones++;
            if (tablaHash[i] == clave) {
                return new ResultadoBusqueda(true, "Búsqueda Lineal: Clave " + clave + 
                              " encontrada en posición " + (i + 1) +  // Mostrar posición como 1-based
                              " (Comparaciones: " + comparaciones + ")", 
                              i, comparaciones);
            }
        }
        
        return new ResultadoBusqueda(false, "Búsqueda Lineal: Clave " + clave + 
                       " no encontrada (Comparaciones: " + comparaciones + ")", 
                       -1, comparaciones);
    }
    
    public ResultadoBusqueda buscarClaveBinaria(int clave) {
        // Para búsqueda binaria, necesitamos una copia ordenada
        int[] tablaOrdenada = tablaHash.clone();
        Arrays.sort(tablaOrdenada);
        
        int inicio = 0;
        int fin = numClaves - 1;
        int comparaciones = 0;
        
        while (inicio <= fin) {
            comparaciones++;
            int medio = (inicio + fin) / 2;
            
            if (tablaOrdenada[medio] == clave) {
                return new ResultadoBusqueda(true, "Búsqueda Binaria: Clave " + clave + 
                              " encontrada (Comparaciones: " + comparaciones + ")", 
                              medio, comparaciones);  // Aquí no sumamos 1 porque no referenciamos la posición específica
            } else if (tablaOrdenada[medio] < clave) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        
        return new ResultadoBusqueda(false, "Búsqueda Binaria: Clave " + clave + 
                       " no encontrada (Comparaciones: " + comparaciones + ")", 
                       -1, comparaciones);
    }
    
    public ResultadoOperacion borrarClave(int clave, String metodo) {
        int indice = transformarClave(clave, metodo);
        
        if (tablaHash[indice] == clave) {
            tablaHash[indice] = -1;  // Marcar como borrado
            return new ResultadoOperacion(true, "Clave " + clave + " borrada de la posición " + indice, indice);
        }
        
        return new ResultadoOperacion(false, "La clave " + clave + " no se encuentra en la posición " + indice + " de la tabla.", -1);
    }
    
    public int transformarClave(int clave, String metodo) {
        int indice = 0;
        
        switch (metodo) {
            case "Módulo":
                indice = clave % numClaves;
                break;
            case "Cuadrado":
                int cuadrado = clave * clave;
                String cuadradoStr = String.valueOf(cuadrado);
                // Extraer dígitos del centro
                int inicio = Math.max(0, (cuadradoStr.length() - digitosClaves) / 2);
                String parteMedia = cuadradoStr.substring(
                    inicio, 
                    Math.min(inicio + digitosClaves, cuadradoStr.length())
                );
                indice = Integer.parseInt(parteMedia) % numClaves;
                break;
            case "Plegamiento":
                String claveStr = String.format("%0" + digitosClaves + "d", clave);
                int suma = 0;
                // Dividir en grupos de 2 dígitos y sumar
                for (int i = 0; i < claveStr.length(); i += 2) {
                    int fin = Math.min(i + 2, claveStr.length());
                    suma += Integer.parseInt(claveStr.substring(i, fin));
                }
                indice = suma % numClaves;
                break;
            case "Truncamiento":
                String claveCompleta = String.format("%0" + digitosClaves + "d", clave);
                // Tomar dígitos en posiciones específicas (por ejemplo, primero y último)
                StringBuilder truncado = new StringBuilder();
                truncado.append(claveCompleta.charAt(0));
                truncado.append(claveCompleta.charAt(claveCompleta.length() / 2));
                truncado.append(claveCompleta.charAt(claveCompleta.length() - 1));
                indice = Integer.parseInt(truncado.toString()) % numClaves;
                break;
        }
        
        return indice;
    }
}