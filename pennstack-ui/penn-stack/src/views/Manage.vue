<template>
  <div class="container">
    <div class="products">
      <h3>Manage Technical stacks</h3>
      <p>You chose {{ $store.getters.name }}</p>
      <p>You chose {{ $store.getters.stackList }}</p>
<ul>
  <li v-for="item in filterItems(shoppingItems)" :key="item.name">
    {{ item.name }} - {{ item.price }}
  </li>
</ul>
      <h5>Manage Technical stacks</h5>
<ul>
  <li v-for="sta in filterStacks(stack)"  v-bind:key="sta.data.id" >
          <h5>Manage loop</h5>

    {{ sta.data.id }} - {{ sta.category }}
  </li>
</ul>



      <div v-if="this.errors.length" class="alert alert-danger" role="alert">
        <ul>
          <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
        </ul>
      </div>

      <form id="app" @submit="checkForm" method="post">
        <div class="card">
          <div class="card-header">Add a new Stack</div>
          <div class="card-body">
            <div class="form-inline">
              <div class="form-group">
                <label>Name</label>
                <input
                  type="text"
                  class="form-control ml-sm-2 mr-sm-4 my-2"
                  v-model="stack.name"
                  name="name"
                />
              </div>
              <div class="form-group">
                <label>Category</label>
                <select
                  class="form-control ml-sm-2 mr-sm-4 my-2"
                  id="cat"
                  v-model="stack.category"
                  name="category"
                >
                  <option>Front end</option>
                  <option>Back end</option>
                  <option>Cloud service provider</option>
                  <option>Build</option>
                  <option>CI/CD</option>
                </select>
              </div>

              <div class="form-group">
                <label>Licence Type</label>
                <select
                  class="form-control ml-sm-2 mr-sm-4 my-2"
                  id="licenceType"
                  v-model="stack.licenceType"
                  name="licenceType"
                >
                  <option>Proprietary software</option>
                  <option>Open source licence</option>
                  <option>Others</option>
                </select>
              </div>
              <div class="ml-auto text-right">
                <button v-on:click="saveStack" class="btn btn-primary my-2">Add</button>
              </div>
            </div>
          </div>
        </div>
      </form>
      <div class="card mt-5">
        <div class="card-header">Stack List</div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table">
              <thead>
                <tr>
                  <th scope="col">Stack ID</th>
                  <th>Stack Name</th>
                  <th>Category</th>
                  <th>Licence Type</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody @stack-list="getStackList">
                <tr v-for="st in stack" v-bind:key="st.id">
                  <template v-if="stack.length>0">
                    <td>{{st.id}}</td>
                    <td>{{st.name}}</td>
                    <td>{{st.category}}</td>
                    <td>{{st.licenceType}}</td>
                    <td>
                      <i class="far fa-edit" aria-hidden="true" @click="editStack(st,st.id)"></i>
                      <i class="fa fa-trash" aria-hidden="true" @click="deleteStack(st,st.id)"></i>
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
h3 {
  text-align: center;
  margin-top: 30px;
  margin-bottom: 20px;
}
.icon {
  margin-right: 10px;
}
.icon i {
  cursor: pointer;
}
</style>
<script>
import http from "../http-common";
import CustomComponentModal from "@/components/CustomComponentModal.vue";
import axios from "axios";
import store from '../store/store';
import { mapState } from 'vuex';
import * as type from '../store/mutationtypes/types';


export default {
  components: {
    CustomComponentModal,
    axios
  },
  data() {
    return {
      errors: [],
      stack: {
        id: 0,
        category: "",
        name: "",
        licenceType: ""
      },
     shoppingItems: [
      {name: 'apple', price: '7'},
      {name: 'orange', price: '12'}
    ] 
    };
  },
  methods: {

    filterItems: function(items) {
    return items.filter(function(item) {
      return item.price > 10;
    })
  }, 
   filterStacks: function(items) {
     alert("filterStacks-->"+JSON.stringify(items));
    return items.filter(function(item) {
      return item.id > 0;
    })
  }, 
    /* eslint-disable no-console */
    saveStack() {
      var data = {
        name: this.stack.name,
        category: this.stack.category,
        licenceType: this.stack.licenceType
      };
      if (this.errors.length <= 0) {
        alert("saasasasa3");

        http
          .post("/stack/", data)
          .then(response => {
            store.dispatch({
            type: type.ADDSTACK,
            response  
          })
          
          })
          .catch(e => {
            console.log(e);
          });
             }
    },

    checkForm: function(e) {
      e.preventDefault();

      this.errors = [];

      if (!this.stack.name) {
        this.errors.push("Name required.");
      }
      if (!this.stack.category) {
        this.errors.push("Category required.");
      }
      if (!this.stack.licenceType) {
        this.errors.push("LicenceType required.");
      }
      if (!this.errors.length) {
        return true;
      }
    },
    getStackList() {
      var data = {
      };

      http
        .get("http://localhost:8080/stacks/", data)
        .then(response => {
          alert("sdsdsd"+JSON.stringify( response.data));
          this.$store.commit("update", response.data);
          this.stack = this.$store.getters.stackList;
          alert("From store"+JSON.stringify(this.$store.getters.stackList));
          alert("From store1"+JSON.stringify(this.stack));
        })
        .catch(e => {
          console.log(e);
        });
    },
    deleteStack(stack, id) {
      http
        .delete("/stack/" + id)
        .then(response => {
          alert("stack before"+JSON.stringify(this.stack));
        let deleted= this.stack.splice(id, 1);
          let deletednew= this.$delete(this.stack, id);
          alert("stack deleted "+JSON.stringify(deleted));
          alert("stack deletednew "+JSON.stringify(deletednew));

          alert("stack after "+JSON.stringify(this.stack));

         // this.$store.commit("update", this.stack);
         // this.stack = this.$store.getters.stackList;
        // getStackList();
         store.dispatch({
            type: type.DELETESTACK,
            deleted
          })
        })
        .catch(e => {
          console.log(e);
        });
    },

    editStack(st, id) {
      //let editid =this.stack.id;
      console.log(JSON.stringify(st));
      let name = st.name;
      let cat = st.category;
      let lType = st.licenceType;
      alert(name + cat + lType);

      http
        .put("/stack/" + id, {
          id: +id,
          name: +name + "updated",
          category: +cat,
          licenceType: +lType
        })
        .then(response => {
          this.stack.splice(id, 1);
          this.$store.commit("update", response.data);
          this.stack = this.$store.getters.stackList;
        })
        .catch(e => {
          console.log(e);
        });
    }
    /* eslint-enable no-console */
  },

  mounted() {
    // alert("mounted");
    this.errors = [];
    this.getStackList();
  }

};
</script>

