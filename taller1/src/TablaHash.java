//tablahash.java

import java.io.Serializable;
import java.util.Arrays;

public class TablaHash implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int[] tablaHash;
    private int numClaves;
    private int digitosClaves;
    private String metodoInsercion;
    private String metodoColision;
    
    public TablaHash(int numClaves, int digitosClaves, String metodoInsercion, String metodoColision) {
        this.numClaves = numClaves;
        this.digitosClaves = digitosClaves;
        this.metodoInsercion = metodoInsercion;
        this.metodoColision = metodoColision;
        this.tablaHash = new int[numClaves];
        Arrays.fill(tablaHash, -1);  // -1 indica posición vacía
    }
    
    public int getNumClaves() {
        return numClaves;
    }
    
    public int getDigitosClaves() {
        return digitosClaves;
    }
    
    public int[] getTablaHash() {
        return tablaHash;
    }
    
    public String getMetodoInsercion() {
        return metodoInsercion;
    }
    
    public String getMetodoColision() {
        return metodoColision;
    }
    private int buscarClaveEnTabla(int clave) {
        for (int i = 0; i < tablaHash.length; i++) {
            if (tablaHash[i] == clave) {
                return i;
            }
        }
        return -1; // No encontrada
    }
    // Método para resolver colisiones
private ResultadoOperacion resolverColision(int clave, int indiceOriginal, String metodo) {
    int intentos = 1;
    int indice = indiceOriginal;
    
    while (intentos < numClaves) {
        if (metodoColision.equalsIgnoreCase("Lineal")) {
            indice = (indiceOriginal + intentos) % numClaves;
        } else if (metodoColision.equalsIgnoreCase("Cuadrática")) {
            indice = (indiceOriginal + intentos * intentos) % numClaves;
        } else if (metodoColision.equalsIgnoreCase("Doble Hash")) {
            // Implementación simple de doble hash
            int hash2 = 1 + (clave % (numClaves - 1));
            indice = (indiceOriginal + intentos * hash2) % numClaves;
        }
        
        if (tablaHash[indice] == -1) {
            tablaHash[indice] = clave;
            return new ResultadoOperacion(true, 
                "Clave " + clave + " insertada en posición " + (indice + 1) + 
                " (después de " + intentos + " intentos por colisión)", indice);
        }
        
        intentos++;
    }
    
    return new ResultadoOperacion(false, 
        "No se pudo insertar la clave " + clave + " después de " + intentos + " intentos", -1);
}
    // Agregar/reemplazar en la clase TablaHash
public ResultadoOperacion insertarClave(int clave, String metodo) {
    // Transformar clave según método seleccionado
    int indice = transformarClave(clave, metodo);
    
    // Verificar si la clave ya existe
    int posicion = buscarClaveEnTabla(clave);
    if (posicion != -1) {
        return new ResultadoOperacion(false, 
            "La clave " + clave + " ya existe en la posición " + (posicion + 1), posicion);
    }
    
    // Si es método de inserción hash, intentar directamente
    if (metodoInsercion.equalsIgnoreCase("Hash")) {
        if (tablaHash[indice] == -1) {
            tablaHash[indice] = clave;
            return new ResultadoOperacion(true, 
                "Clave " + clave + " insertada en posición " + (indice + 1), indice);
        } else {
            // Resolver colisión según el método configurado
            return resolverColision(clave, indice, metodo);
        }
    } 
    // Si es inserción lineal, buscar primera posición libre
    else if (metodoInsercion.equalsIgnoreCase("Lineal")) {
        // Corrección: Buscar la primera posición libre empezando desde 0
        for (int i = 0; i < numClaves; i++) {
            if (tablaHash[i] == -1) {
                tablaHash[i] = clave;
                return new ResultadoOperacion(true, 
                    "Clave " + clave + " insertada en posición " + (i + 1), i);
            }
        }
        return new ResultadoOperacion(false, "Tabla llena, no se pudo insertar la clave " + clave, -1);
    } 
    // Si es inserción binaria
    else if (metodoInsercion.equalsIgnoreCase("Binaria")) {
        // Implementación para inserción binaria
        // ...código para inserción binaria...
        return new ResultadoOperacion(false, "Método de inserción binaria no implementado", -1);
    } 
    else {
        return new ResultadoOperacion(false, "Método de inserción desconocido", -1);
    }
}
    // Método para buscar una clave en la tabla



    // Método para determinar la siguiente posición según método de colisión
    private int siguientePosicion(int indiceOriginal, int intento, String metodo) {
        switch (metodoColision) {
            case "Lineal":
                // Sondeo lineal: indice = (h(k) + i) % m
                return (indiceOriginal + intento) % numClaves;
                
            case "Cuadrática":
                // Sondeo cuadrático: indice = (h(k) + i²) % m
                return (indiceOriginal + intento * intento) % numClaves;
                
            case "Doble Hash":
                // Doble hash: indice = (h1(k) + i * h2(k)) % m
                // Para h2, usamos un método simple: (clave % (m-1)) + 1 para garantizar valor > 0
                int clave = tablaHash[indiceOriginal]; // Obtener la clave que causó la colisión
                if (clave == -1) clave = indiceOriginal; // Si no hay clave, usar el índice como respaldo
                int h2 = (clave % (numClaves - 1)) + 1;
                return (indiceOriginal + intento * h2) % numClaves;
                
            default:
                // Por defecto, usar sondeo lineal
                return (indiceOriginal + intento) % numClaves;
        }
    }
    
    public ResultadoBusqueda buscarClaveHash(int clave, String metodo) {
        int indiceOriginal = transformarClave(clave, metodo);
        int indice = indiceOriginal;
        int comparaciones = 0;
        int intentos = 0;
        
        while (intentos < numClaves && tablaHash[indice] != -1) {
            comparaciones++;
            
            if (tablaHash[indice] == clave) {
                return new ResultadoBusqueda(true, 
                    "Búsqueda por Transformación: Clave " + clave + 
                    " encontrada en posición " + (indice + 1) + 
                    " (Comparaciones: " + comparaciones + ")", 
                    indice, comparaciones);
            }
            
            // Si no es la clave y estamos usando un método de colisión, probar siguiente posición
            if (metodoInsercion.equals("Hash")) {
                indice = siguientePosicion(indiceOriginal, ++intentos, metodo);
                indice = indice % numClaves;
            } else {
                // Si no es inserción hash, no seguimos buscando
                break;
            }
        }
        
        return new ResultadoBusqueda(false, 
            "Búsqueda por Transformación: Clave " + clave + 
            " no encontrada (Comparaciones: " + comparaciones + ")", 
            -1, comparaciones);
    }
    
    public ResultadoBusqueda buscarClaveLineal(int clave) {
        int comparaciones = 0;
        
        for (int i = 0; i < numClaves; i++) {
            comparaciones++;
            if (tablaHash[i] == clave) {
                return new ResultadoBusqueda(true, 
                    "Búsqueda Lineal: Clave " + clave + 
                    " encontrada en posición " + (i + 1) +
                    " (Comparaciones: " + comparaciones + ")", 
                    i, comparaciones);
            }
        }
        
        return new ResultadoBusqueda(false, 
            "Búsqueda Lineal: Clave " + clave + 
            " no encontrada (Comparaciones: " + comparaciones + ")", 
            -1, comparaciones);
    }
    
    public ResultadoBusqueda buscarClaveBinaria(int clave) {
        // Para búsqueda binaria, necesitamos una copia ordenada
        int[] tablaOrdenada = Arrays.stream(tablaHash).filter(v -> v != -1).toArray();
        Arrays.sort(tablaOrdenada);
        
        int inicio = 0;
        int fin = tablaOrdenada.length - 1;
        int comparaciones = 0;
        
        while (inicio <= fin) {
            comparaciones++;
            int medio = (inicio + fin) / 2;
            
            if (tablaOrdenada[medio] == clave) {
                // Buscar la posición original en la tabla hash
                for (int i = 0; i < numClaves; i++) {
                    if (tablaHash[i] == clave) {
                        return new ResultadoBusqueda(true, 
                            "Búsqueda Binaria: Clave " + clave + 
                            " encontrada en posición " + (i + 1) + 
                            " (Comparaciones: " + comparaciones + ")", 
                            i, comparaciones);
                    }
                }
            } else if (tablaOrdenada[medio] < clave) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        
        return new ResultadoBusqueda(false, 
            "Búsqueda Binaria: Clave " + clave + 
            " no encontrada (Comparaciones: " + comparaciones + ")", 
            -1, comparaciones);
    }
    
    public ResultadoBusqueda buscarClave(int clave, String metodo) {
        switch (metodoInsercion) {
            case "Hash":
                return buscarClaveHash(clave, metodo);
            case "Lineal":
                return buscarClaveLineal(clave);
            case "Binaria":
                return buscarClaveBinaria(clave);
            default:
                return buscarClaveHash(clave, metodo);
        }
    }
    
    public ResultadoOperacion borrarClave(int clave, String metodo) {
        ResultadoBusqueda resultadoBusqueda = buscarClave(clave, metodo);
        
        if (resultadoBusqueda.isExito()) {
            int indice = resultadoBusqueda.getPosicion();
            tablaHash[indice] = -1;  // Marcar como borrado
            return new ResultadoOperacion(true, 
                "Clave " + clave + " borrada de la posición " + (indice + 1), 
                indice);
        }
        
        return new ResultadoOperacion(false, 
            "La clave " + clave + " no se encuentra en la tabla.", 
            -1);
    }
    
// Este método debe estar en la clase TablaHash
public int transformarClave(int clave, String metodo) {
    if (metodo.equalsIgnoreCase("Módulo")) {
        return clave % numClaves;
    } 
    else if (metodo.equalsIgnoreCase("Cuadrado")) {
        long cuadrado = (long) clave * clave;
        String strCuadrado = String.valueOf(cuadrado);
        
        // Si el cuadrado tiene suficientes dígitos, tomar dígitos centrales
        if (strCuadrado.length() > 2) {
            int inicio = (strCuadrado.length() - String.valueOf(numClaves).length()) / 2;
            String subcadena = strCuadrado.substring(inicio, inicio + String.valueOf(numClaves).length());
            int valorHash = Integer.parseInt(subcadena) % numClaves;
            return valorHash;
        } else {
            return (int) (cuadrado % numClaves);
        }
    } 
    else if (metodo.equalsIgnoreCase("Plegamiento")) {
        // Corregir el método de plegamiento
        String claveStr = String.valueOf(clave);
        int suma = 0;
        
        // Asegurarse de que la clave tenga al menos 2 dígitos
        if (claveStr.length() < 2) {
            return clave % numClaves; // Si es un solo dígito, usar módulo simple
        }
        
        // Dividir en grupos de 2 dígitos y sumar
        for (int i = 0; i < claveStr.length(); i += 2) {
            if (i + 2 <= claveStr.length()) {
                suma += Integer.parseInt(claveStr.substring(i, i + 2));
            } else if (i + 1 <= claveStr.length()) {
                suma += Integer.parseInt(claveStr.substring(i, i + 1));
            }
        }
        
        return suma % numClaves;
    } 
    else if (metodo.equalsIgnoreCase("Truncamiento")) {
        String claveStr = String.valueOf(clave);
        StringBuilder resultado = new StringBuilder();
        
        // Tomar los dígitos en posiciones impares o primeros dígitos si es corta
        if (claveStr.length() >= 3) {
            for (int i = 0; i < claveStr.length(); i += 2) {
                resultado.append(claveStr.charAt(i));
            }
        } else {
            resultado.append(claveStr);
        }
        
        int valorHash = Integer.parseInt(resultado.toString()) % numClaves;
        return valorHash;
    } 
    else {
        // Método no reconocido, usar módulo por defecto
        return clave % numClaves;
    }
}
}