
<template>
  <div>
    <div class="headerNav">
 <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="#">Pennstack</a>
        <button
          class="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
              <router-link class="nav-link" to="/">Home</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" active-class="" to="/manage">Manage</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" active-class="" to="/dashboard">Dashboard</router-link>
            </li>
             

            <li class="nav-item">
              <router-link class="nav-link" to="/report">Report</router-link>
            </li>
          </ul>
          <form class="form-inline my-2 my-lg-0">
            <input
              class="form-control mr-sm-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
            >
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
          </form>
        </div>
      </nav>
      <div class="jumbotron jumbotron-fluid">
        <div class="container">
          <h1 class="display-6">PennStack</h1>
          <p class="lead">Tech stack</p>
        </div> 
      </div>
    </div>
    
  </div>
  
</template>
<style scoped>
.header {
  margin: 0px;
  /* background-color: white;*/
}
body {
  margin: 0;
}
.headerNav {
  /* margin-top: -40px;*/
}
.jumbotron {
    
background-image: url(../assets/header.jpg);
background-size: cover;
height: 100%;
color: white;
}
</style>
<script>
export default {};
</script>
>