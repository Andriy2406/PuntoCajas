/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Permisos;
import java.sql.*;

/**
 *
 * @author Nelson
 */
public class PermisosDAO {

    private Conexion conect = new Conexion();

    public Permisos consultarPermisos(int idPermiso) {

        Permisos miPermiso = null;

        Connection conn = conect.conn();

        try {
            String querySql = "SELECT id_permiso, nombre, descripcion FROM permisos WHERE id_permiso = ?";

            PreparedStatement ps = conn.prepareStatement(querySql);

            ps.setInt(1, idPermiso);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                miPermiso = new Permisos();
                miPermiso.setIdPermiso(rs.getInt("id_permiso"));
                miPermiso.setNombre(rs.getString("nombre"));
                miPermiso.setDescripcion(rs.getString("descripcion"));
            }

        } catch (Exception e) {
            System.out.println("Error al consultar los permisos " + e.getMessage());
        }
        return miPermiso;

    }

    public boolean insertarPermisos(Permisos miPermiso) {

        boolean insertar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "INSERT INTO permisos (nombre, descripcion) VALUES (?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setString(1, miPermiso.getNombre());
            ps.setString(2, miPermiso.getDescripcion());
            
            ps.executeUpdate();
            insertar = true;
            System.out.println("Se ingresaron los datos con exito");
            
        } catch (Exception e) {
            System.out.println("No se pudo insertar ningun permsio" + e.getMessage());
        }
        return insertar;

    }
    
    public boolean actualizarPermisos (Permisos miPermisos){
    
        boolean actualizar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "UPDATE permisos SET nombre = ?, descripcion = ? WHERE id_permiso = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            ps.setString(1, miPermisos.getNombre());
            ps.setString(2, miPermisos.getDescripcion());
            ps.setInt(3, miPermisos.getIdPermiso());
            
            if(ps.executeUpdate() > 0) {
                actualizar = true;
                System.out.println("Registro actualizado");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar permisos " + e.getMessage());
        }
        return actualizar;
    }
    
    public boolean eliminarPermisos (int idPermiso){
    
        boolean eliminar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "DELETE FROM permisos WHERE id_permiso = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, idPermiso);
            
            int filaEliminada = ps.executeUpdate();
            
            if(filaEliminada > 0){
                eliminar = true;
            }else{
                System.out.println("Error al econtrar el ID");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario " + e.getMessage());
        }
        return eliminar;
    }
}
