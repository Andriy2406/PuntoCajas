/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.TiposDocCon;
import java.sql.*;

/**
 *
 * @author Nelson
 */
public class TiposDocConDAO {

    private Conexion conect = new Conexion();

    // Entrega el siguiente número de factura y avanza el consecutivo en una sola
    // operación atómica (UPDATE ... RETURNING), para que dos facturas creadas al
    // mismo tiempo nunca puedan recibir el mismo número.
    public TiposDocCon obtenerYAvanzarConsecutivo(int codigoCon) {

        TiposDocCon resultado = null;

        Connection conn = conect.conn();

        try {
            String querySql = "UPDATE tipos_doc_con SET numero_actual = numero_actual + 1 "
                    + "WHERE codigo_con = ? RETURNING id_doc_con, codigo_con, numero_actual";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, codigoCon);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resultado = new TiposDocCon();
                resultado.setIdDocCon(rs.getInt("id_doc_con"));
                resultado.setCodigoCon(rs.getInt("codigo_con"));
                resultado.setNumeroActual(rs.getInt("numero_actual"));
            } else {
                System.out.println("No existe un tipos_doc_con con codigo_con = " + codigoCon);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el consecutivo de facturación: " + e.getMessage());
        }
        return resultado;
    }

    public TiposDocCon consultarTiposDocCon(int codigoCon) {

        TiposDocCon miTipoDocCon = null;

        Connection conn = conect.conn();

        try {

            String querySql = "SELECT id_doc_con, codigo_con, numero_actual FROM tipos_doc_con WHERE codigo_con = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, codigoCon);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                miTipoDocCon = new TiposDocCon();
                miTipoDocCon.setIdDocCon(rs.getInt("id_doc_con"));
                miTipoDocCon.setCodigoCon(rs.getInt("codigo_con"));
                miTipoDocCon.setNumeroActual(rs.getInt("numero_actual"));

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
        return miTipoDocCon;
    }

    public boolean insertarTipoDocCon(TiposDocCon miTipoDocCon) {

        boolean insertar = false;

        Connection conn = conect.conn();

        try {

            String querySql = "INSERT INTO tipos_doc_con (codigo_con, numero_actual) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, miTipoDocCon.getCodigoCon());
            ps.setInt(2, miTipoDocCon.getNumeroActual());

            ps.executeUpdate();
            insertar = true;
            System.out.println("TipoDonCon creado con exito");

        } catch (Exception e) {
            System.out.println("Error al insertar el TipoDocCon" + e.getMessage());
        }
        return insertar;
    }

    public boolean actualizarTipoDocCon(TiposDocCon miTiposDocCon) {

        boolean actualizar = false;

        Connection conn = conect.conn();
        
        try {
            String querySql = "UPDATE tipos_doc_con SET codigo_con = ?, numero_actual = ? WHERE id_doc_con = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, miTiposDocCon.getCodigoCon());
            ps.setInt(2, miTiposDocCon.getNumeroActual());
            ps.setInt(3, miTiposDocCon.getIdDocCon());
            
            if(ps.executeUpdate() > 0) {
            
                actualizar = true;
                System.out.println("Registro actualizado");
            
            }
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar el tipo de documento contable " + e.getMessage());
        }
        return actualizar;
    }
    
    public boolean eliminarTipoDocCon(int idDocCon){
    
        boolean eliminar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "DELETE FROM tipos_doc_con WHERE id_doc_con = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, idDocCon);
            
            int filaEliminada = ps.executeUpdate();
            
            if(filaEliminada > 0) {
                eliminar = true;
            }else {
                System.out.println("Error al encontrar el ID");
            }
                    
        } catch (SQLException e) {
            System.out.println("Error al elimianr el usuario " + e.getMessage());
        }
        return eliminar;
    }
}
