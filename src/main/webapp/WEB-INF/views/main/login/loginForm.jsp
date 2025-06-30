<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
		<form:form method="post">
        <div class="card my-5">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-end mb-4">
              <h3 class="mb-0"><b>Login</b></h3>
              <a href="/member/memberInsert.do" class="link-primary">Don't have an account?</a>
            </div>
            <div class="form-group mb-3">
              <label class="form-label">Username</label>
              <input type="text" name="username" class="form-control" placeholder="Username">
            </div>
            <div class="form-group mb-3">
              <label class="form-label">Password</label>
              <input type="password" name="password" class="form-control" placeholder="Password">
            </div>
            <div class="d-flex mt-1 justify-content-between">
              <div class="form-check">
                <input class="form-check-input input-primary" type="checkbox" id="customCheckc1" checked="">
                <label class="form-check-label text-muted" for="customCheckc1">Keep me sign in</label>
              </div>
              <h5 class="text-secondary f-w-400">Forgot Password?</h5>
            </div>
            <div class="d-grid mt-4">
              <button type="submit" class="btn btn-primary">Login</button>
            </div>
            <div class="saprator mt-3">
              <span>Login with</span>
            </div>
            <div class="row">
              <div class="col-4">
                <div class="d-grid">
                  <a href="/oauth2/authorization/google-login" class="btn mt-2 btn-light-primary bg-light text-muted">
                    <img src="/dist/assets/images/authentication/google.svg" alt="img"> 
                    <span class="d-none d-sm-inline-block"> Google</span>
                  </a>
                </div>
              </div>
              <div class="col-4">
                <div class="d-grid">
                  <button type="button" class="btn mt-2 btn-light-primary bg-light text-muted">
                    <img src="/dist/assets/images/authentication/twitter.svg" alt="img"> <span class="d-none d-sm-inline-block"> Twitter</span>
                  </button>
                </div>
              </div>
              <div class="col-4">
                <div class="d-grid">
                  <button type="button" class="btn mt-2 btn-light-primary bg-light text-muted">
                    <img src="/dist/assets/images/authentication/facebook.svg" alt="img"> <span class="d-none d-sm-inline-block"> Facebook</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
	</form:form>
</body>
</html>
