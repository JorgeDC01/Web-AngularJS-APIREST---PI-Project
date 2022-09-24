<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css2/tailwind.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css2/background.css" />
<title>Registrarse en Airbnb</title>
</head>

<body>
	<div
		class="min-h-screen flex flex-col items-center justify-center 
     bg-cover login">
		<div
			class="
    flex flex-col
    bg-white
    shadow-md
    px-8
    py-20
    mt-1
  	mb-1
    rounded-3xl
    w-96
    max-w-md
    ">
			<h2>${messages.error}</h2>
			<div class="relative top-1/4">
				<div
					class="relative left-0  self-center bottom-1/3 font-medium text-gray-800">
					<c:choose>
      				<c:when test="${not empty user.id}"> <h1 id="cabeceraRegistro">Confirmar Registro</h1></c:when>
      				
    				<c:otherwise> <h1 id="cabeceraRegistro">Regístrate</h1></c:otherwise>
					</c:choose>
				</div>
				<div class="mt-10 class relative bottom-1/3">
					<form action="?" method="POST">
						<!-------- NOMBRE -------->
						<div class="flex flex-col mb-2">
							<div class="relative">
								<input id="username" type="text" name="username"
									class="
                        text-sm
                        placeholder-gray-500
                        pl-10
                        pr-4
                        rounded-2xl
                        border border-gray-400
                        w-full
                        py-2
                        focus:outline-none focus:border-blue-400
                        "
									placeholder="ID" value="${user.username}" required
									<c:if test="${not empty user.id}">readonly</c:if>/>
							</div>
						</div>
						<!-------- CORREO -------->
						<div class="flex flex-col mb-3">
							<div class="relative">
								<input id="email" type="email" name="email"
									class="
                        text-sm
                        placeholder-gray-500
                        pl-10
                        pr-4
                        rounded-2xl
                        border border-gray-400
                        w-full
                        py-2
                        focus:outline-none focus:border-blue-400
                        "
									placeholder="Correo" value="${user.email}" required
									<c:if test="${not empty user.id}">readonly</c:if>/>
							</div>
							<div class="flex flex-col mb-9">
								<div
									class="inline-flex
                        items-center
                        justify-center
                        absolute
                        w-auto
                        font-sans
                        text-xs
                        text-gray-400
                        text-justify">Te
									enviaremos por correo electronico las confirmaciones y los
									recibos de los viajes</div>
							</div>
						</div>
						<!-------- CONTRASEÑA -------->
						<div class="flex flex-col mb-9">
							<div class="relative">
								<input id="password" type=
								<c:choose>
								<c:when test="${not empty user.id}">"text"</c:when>
		    					<c:otherwise> "password"</c:otherwise>
								</c:choose>
							name="password"
							class="
                        text-sm
                        placeholder-gray-500
                        pl-10
                        pr-4
                        rounded-2xl
                        border border-gray-400
                        w-full
                        py-2
                        focus:outline-none focus:border-blue-400
                        "
									placeholder="Contraseña" value="${user.password}" required
									<c:if test="${not empty user.id}">readonly</c:if>/>
							</div>
							<div class="flex flex-col mb-16">
								<div
									class="inline-flex
                            items-center
                            justify-center
                            absolute
                            w-auto
                            font-sans
                            text-xs
                            text-gray-400">
									<p class="text-justify">
										Al seleccionar <b>Registrarse</b>, acepto las Condiciones del
										servicio, las Condiciones sobre pagos y la Política contra la
										Discriminación de Airbnb, así como su Política de Privacidad.
									</p>
								</div>
							</div>
						</div>
						<!-- BOTÓN REGISTRARSE -->
						<input id="button" type="submit" class=" flex mb-4 
						uppercase
						mt-2
						items-center
						justify-center
						focus:outline-none
						text-white
						text-sm
						bg-blue-500
						hover:bg-blue-600
						rounded-2xl
						py-2 w-full
						transition duration-150
						ease-in " value=<c:choose>
      								    <c:when test="${not empty user.id}"> "Confirmar registro"</c:when>
    								    <c:otherwise> "Registrarse"</c:otherwise>
					  				    </c:choose> />
						<!-- CORREOS Y AVISOS DE PROMOCIÓN -->
						<div class="flex-col relative">
							<div class="flex flex-col mb-8">
								<div
									class="inline-flex
                items-center
                justify-center
                absolute
                w-auto
                font-sans
                text-xs
                text-gray-400">
									<p class="text-justify">Airbnb te enviará ideas para
										inspirarte, correos promocionales, notificaciones push y
										ofertas exclusivas para los miembros de la comunidad. Puedes
										darte de baja en cualquier momento desde los ajustes de tu
										cuenta o directamente desde alguna de las notificaciones que
										te llegue.</p>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>