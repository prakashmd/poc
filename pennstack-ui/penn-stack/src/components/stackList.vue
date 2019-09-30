<template>
 <div class="container">

  <div class="products">
   <div class="card mt-5">
      <div class="card-header">
        Stack List
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th scope="col">
                  Stack ID
                </th>
                <th>
                  Stack Name
                </th>
                <th>
                  Product Price
                </th>
                <th>
                  Action
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(st, index) in stack" :key="index">
               
                <template>
                  <td>{{st.id}}
                  </td>
                  <td>{{st.name}}
                  </td>
                  <td>{{st.licenetype}}
                  </td>
                  <td>

                  
                    <a href="#" class="icon">
                      <i class="fa fa-pencil"></i>
                    </a>
                    <router-link 
                    
                    class="icon"
                    >
                      <i class="fa fa-eye"></i>
                    </router-link>
                  </td>
                </template>
              </tr>

            </tbody>
          </table>

        </div>
      </div>
    </div>
    </div>
 </div>
</template>



<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3{
  text-align: center;
  margin-top: 30px;
  margin-bottom: 20px;
}
.icon{
  margin-right: 10px;
}
.icon i{
  cursor: pointer;
}
</style>
<script>
import http from "../http-common";

export default {
  name: "add-stack",
  data() {
    return {
      stack: {
        id: 0,
        category: "",
        name: "",
       licensetype:""
      },
      
    };
  },
  methods: {
    /* eslint-disable no-console */
   
    getStackList() {
      var data = {
        category: [],
      };

      http
        .get("/stack/", data)
        .then(response => {
          this.stack= response.data;
          console.log("getStackList->",response.data);
        })
        .catch(e => {
          console.log(e);
        });

      this.submitted = true;
    }
    /* eslint-enable no-console */
  }
};
</script>

