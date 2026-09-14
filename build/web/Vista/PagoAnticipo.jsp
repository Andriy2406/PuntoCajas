<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Pago de anticipo</title>
    </head>

    <body>

        <h1>Pago de anticipo</h1>

        <p>
            Cotización:
            <strong>${idCotizacion}</strong>
        </p>

        <p>
            Anticipo a pagar:
            <strong>$${anticipo}</strong> COP
        </p>

        <p>
            Referencia de pago:
            <strong>${reference}</strong>
        </p>

        <form action="${checkoutUrl}" method="GET">


            <button type="submit">
                Pagar anticipo con Mercado Pago
            </button>

        </form>

    </body>
</html>
