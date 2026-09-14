/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.RolPermisos;
import java.sql.*;

/**
 *
 * @author Nelson
 */
public class RolPermisosDAO {

    private Conexion conect = new Conexion();

    public RolPermisos consultarRolPermisos(int idRol) {

        RolPermisos miRolPermisos = null;

        Connection conn = conect.conn();

        try {
            String querySql = "SELECT id_rol, id_permiso FROM permisos WHERE id_rol = ?";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, idRol);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                
                miRolPermisos = new RolPermisos();
                
                miRolPermisos.setIdRol(rs.getInt("id_rol"));
                miRolPermisos.setIdPermiso(rs.getInt("id_permiso"));
                
            }

        } catch (SQLException e) {
            System.out.println("No se pudieron consultar los id" + e.getMessage());
        }
        return miRolPermisos;   
    }
    
    public boolean insertarRolPermisos (RolPermisos MiRolPermisos) {
    
        boolean insertar = false;
        
        Connection conn = conect.conn();
        
        try {
            String querySql = "INSERT INTO rol_permisos (id_rol, id_permiso) VALUES (?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(querySql);
            
            ps.setInt(1, MiRolPermisos.getIdRol());
            ps.setInt(2, MiRolPermisos.getIdPermiso());
            
            ps.executeUpdate();
            insertar = true;
            System.out.println("Datos ingresados con exito");
            
        } catch (SQLException e) {
            System.out.println("Problema al insertar datos" + e.getMessage());
        }
        return insertar;
    
    }
    
}
