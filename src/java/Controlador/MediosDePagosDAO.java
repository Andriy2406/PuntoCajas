/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.MediosDePagos;
import java.sql.*;

/**
 *
 * @author Nelson
 */
public class MediosDePagosDAO {

    private Conexion conect = new Conexion();

    public MediosDePagos consultarMediosDePago(int idMedioPago) {

        MediosDePagos miMedioDePago = null;

        Connection conn = conect.conn();

        try {
            String querySql = "SELECT id_medio_pago, seleccionar_pago FROM medios_de_pagos WHERE id_medio_pago = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setInt(1, idMedioPago);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                miMedioDePago = new MediosDePagos();
                miMedioDePago.setIdMedioPago(rs.getInt("id_medio_pago"));
                miMedioDePago.setSeleccionarPago(rs.getString("seleccionar_pago"));

            }

        } catch (SQLException e) {
            System.out.println("No se pudo realizar la busqueda del medio de pago" + e.getMessage());
        }
        return miMedioDePago;
    }
    public boolean insertarMedioDePago (MediosDePagos miMedioDePago){
    
        boolean insertar = false;
        
        Connection conn = conect.conn();
        
        try {
            
            String querySql = "INSERT INTO medios_de_pagos (seleccionar_pago) VALUES (?)";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setString(1, miMedioDePago.getSeleccionarPago());
            
            ps.executeUpdate();
            insertar = true;
            System.out.println("medio de pago insertado");
        
        
        }catch(SQLException e){
            System.out.println("No se pudo insertar ningun dato en los medios de pago" + e.getMessage());
        }
        return insertar;
    }
    
    public boolean actualizarMediosDePago (MediosDePagos miMediosDePagos){
    
        boolean actualizar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "UPDATE medios_de_pagos SET seleccionar_pago = ? WHERE id_medio_pago = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setString(1, miMediosDePagos.getSeleccionarPago());
            ps.setInt(2, miMediosDePagos.getIdMedioPago());
            
            if(ps.executeUpdate() > 0){
                actualizar = true;
                System.out.println("Registro actualizado");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el registro " + e.getMessage());
        }
        return actualizar;
    }
    
    public boolean eliminarMediosDePagos(int idMedioPago){
    
        boolean eliminar = false;
        
        Connection conn = conect.conn();
        
        try {
        
            String querySql = "DELETE FROM medios_de_pagos WHERE id_medio_pago = ? ";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, idMedioPago);
            
            int filaEliminada = ps.executeUpdate();
            
            if(filaEliminada > 0){
                eliminar = true;
            }else {
                System.out.println("Error al encontrar el ID");
            }
        
        }catch(SQLException e){
            System.out.println("Error al eliminar el medio de pago " + e.getMessage());
        }
        return eliminar;
    }
    public int buscarIdPorNombre(String nombre) {
        try(Connection conn=conect.conn(); PreparedStatement ps=conn.prepareStatement("SELECT id_medio_pago FROM medios_de_pagos WHERE LOWER(seleccionar_pago)=LOWER(?)")){
            ps.setString(1,nombre); try(ResultSet rs=ps.executeQuery()){return rs.next()?rs.getInt(1):0;}
        }catch(SQLException e){System.out.println("Error al buscar medio de pago: "+e.getMessage());return 0;}
    }

}
