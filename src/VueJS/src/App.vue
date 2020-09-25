<template>
  <v-app>
    <v-card color="grey lighten-4" flat tile>
      <v-toolbar dense>
        <v-toolbar-title>Blogs</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-text-field 
          v-model="searchStr" 
          hide-details 
          dense 
          single-line 
          outlined 
          prepend-inner-icon="mdi-magnify" 
          placeholder="Search..."
        ></v-text-field>
        <v-spacer></v-spacer>
        <v-overflow-btn 
          :items="filterCategories" 
          hide-details 
          dense 
          single-line 
          outlined 
          flat 
          label="Filter by categories" 
          v-model="filterCategory" 
          class="mx-2" 
          width="80">
        </v-overflow-btn>
        <v-btn width="200" outlined @click="changeOrder" class="mx-2">
          {{ orderTitle }}
        </v-btn>
        <v-btn outlined @click="addBlog">
          Add blog
        </v-btn>
      </v-toolbar>
    </v-card>
    <v-dialog v-model="adding" max-width="800">
      <v-card>
        <v-card-title>
          Add blog
        </v-card-title>
        <v-card-text>
          <v-form>
            <v-row>
              <v-col cols="12">
                <v-text-field :rules="blogFields" label="Title" placeholder="Title..." v-model="newTitle"></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12">
                <v-text-field :rules="blogFields" label="Description" placeholder="Description..." v-model="newDescription"></v-text-field>
              </v-col>
            </v-row>            
            <v-row>
              <v-col cols="12">
                 <v-combobox 
                  :clearable="false" 
                  :rules="blogFields" 
                  label="Category" 
                  placeholder="Category..." 
                  v-model="newCategory" 
                  :items="categories"
                ></v-combobox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12">
                <v-menu
                  v-model="booldatepicker"
                  :close-on-content-click="false"
                  :nudge-right="40"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="newDate"
                      label="Date"
                      prepend-icon="event"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="newDate" @input="booldatepicker = false"></v-date-picker>
                </v-menu>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn outlined @click="addBlog">
            Cancel
          </v-btn>
          <v-btn outlined @click="submitBlog">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="editing" max-width="800">
      <v-card>
        <v-card-title>
          Edit blog
        </v-card-title>
        <v-card-text>
          <v-form>
            <v-row>
              <v-col cols="12">
                <v-text-field :rules="blogFields" label="Title" placeholder="Title..." v-model="newTitle"></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12">
                <v-text-field :rules="blogFields" label="Description" placeholder="Description..." v-model="newDescription"></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12">
                <v-combobox 
                  :clearable="false" 
                  :rules="blogFields" 
                  label="Category" 
                  placeholder="Category..." 
                  v-model="newCategory" 
                  :items="categories"
                ></v-combobox>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12">
                <v-menu
                  v-model="booldatepicker2"
                  :close-on-content-click="false"
                  :nudge-right="40"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                      v-model="newDate"
                      label="Date"
                      prepend-icon="event"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="newDate" @input="booldatepicker2 = false"></v-date-picker>
                </v-menu>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn outlined @click="cancelEdit">
            Cancel
          </v-btn>
          <v-btn outlined @click="updateBlog">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-content>
      <v-container fluid>
        <v-row>
          <v-col v-for="blog in filteredBlogs" :key="blog.id" cols="3">
            <v-card :color="isToday(blog)">
              <v-list-item two-line>
                <v-list-item-content>
                  <v-list-item-title class="headline">{{blog.title}}</v-list-item-title>
                  <v-list-item-subtitle>{{blog.category}}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-card-text>
                {{blog.description}}
              </v-card-text>
              <v-card-actions>
                <div class="grey--text subtitle-1">
                  {{ moment(blog.date, "YYYY-MM-DD").format("DD-MM-YYYY") }}
                </div>
                <v-row align="center" justify="end" style="margin: 8px;">
                  <v-icon @click="editBlog(blog)">mdi-pencil</v-icon>
                  <v-icon @click="deleteBlog(blog)">mdi-delete</v-icon>
                </v-row>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import Vue from 'vue'
import axios from 'axios'
import moment from 'moment'

export default {
  name: 'App',
  data: () => ({
    adding: false,
    newTitle: "",
    newDescription: "",
    newCategory: "",
    newDate: new Date().toISOString().substr(0, 10),
    booldatepicker: false,
    booldatepicker2: false,
    editing: false,
    curBlog: {},
    orderTitle: "Ordered by title",
    filterCategory: "All categories",
    blogs: [],
    searchStr: "",
    rules: {
      blogFields: val => !!value || 'Required.'
    }
  }),
  computed: {
    filteredBlogs() {
      let arr = this.blogs.filter(blog => {
        return blog.title.toLowerCase().includes(this.searchStr.toLowerCase()) || blog.description.toLowerCase().includes(this.searchStr.toLowerCase()) || blog.category.toLowerCase().includes(this.searchStr.toLowerCase());
      });
      if(this.filterCategory !== "All categories") {
        arr = arr.filter(blog => {
          return blog.category.includes(this.filterCategory);
        });
      }
      return arr;
    },
    categories() {
      var arr = []
      this.blogs.forEach(blog => {
        if(arr.indexOf(blog.category) === -1 && blog.category !== "") {
          arr.push(blog.category);
        }
      });
      return arr;
    },
    filterCategories() {
      let arr = Array.from(this.categories);
      arr.unshift("All categories");
      return arr;
    }
  },
  methods: {
    getBlogs() {
      axios.get("api/blogs").then(res => {
        this.blogs = res.data.sort((blog1, blog2) => {
          let titleA = blog1.title.toUpperCase();
          let titleB = blog2.title.toUpperCase();
          
          if(titleA < titleB) {
            return -1;
          } else if(titleA > titleB) {
            return 1;
          } else {
            return 0;
          }
        });;
        this.$forceUpdate();
      }).catch(err => {
        console.warn(err);
      });
    },
    addBlog() {
      this.adding = !this.adding;
    },
    submitBlog() {
      if(this.newDescription !== "" && this.newTitle !== "" && this.newCategory !== "") {
        axios.post("api/blogs", {title: this.newTitle, date: this.newDate, description: this.newDescription, category: this.newCategory}).then(res => {
          if(res.data == 0) {
            alert("That blog title has already been taken!");
            return;
          }
          this.newTitle = "";
          this.newDescription = "";
          this.newCategory = "";
          this.newDate = new Date().toISOString().substr(0, 10);
          this.adding = false;
          this.getBlogs();
        }).catch(err => {
          console.warn(err);
        });
      } else {
        this.alertMissing();
      }
    },
    deleteBlog(blog) {
      let r = confirm(`Are you sure you want to the delete the blog with title: '${blog.title}'`);
      if(r) {
        axios.delete(`api/blogs/${blog.id}`).then(res => {
          this.getBlogs();
        }).catch(err => {
          console.warn(err);
        });
      }
    },
    cancelEdit() {
      this.editing = false;
      this.newTitle = "";
      this.newDescription = "";
      this.newCategory = "";
      this.newDate = new Date().toISOString().substr(0, 10);
      this.editId = "";
    },
    editBlog(blog) {
      this.editing = true;
      this.curBlog = blog;
      this.newTitle = blog.title;
      this.newDescription = blog.description;
      this.newCategory = blog.category;
      this.newDate = blog.date;
    },
    updateBlog() {
      if(this.newDescription !== "" && this.newTitle !== "" && this.newCategory !== "") {
        let r = confirm(`Are you sure you want to edit the blog with title: ${this.curBlog.title}`);
        if(r) {
          axios.put(`api/blogs/${this.curBlog.id}`, {
            title: this.newTitle,
            description: this.newDescription,
            category: this.newCategory,
            date: this.newDate
          }).then(res => {
            this.newTitle = "";
            this.newDescription = "";
            this.newCategory = "";
            this.newDate = new Date().toISOString().substr(0, 10);
            this.editId = "";
            this.editing = false;
            this.getBlogs();
          }).catch(err => {
            console.warn(err);
          })
        }
      } else {
        this.alertMissing();
      }
    },
    isToday(blog) {
      let date = moment(blog.date, "YYYY-MM-DD");
      let diff = moment().diff(date, 'days');
      return (diff > 0) ? "grey lighten-3" : "white";
    },
    alertMissing() {
      let str = "Not all information provided: \n" + ((this.newTitle == "") ? "    Add title\n" : "") + ((this.newDescription == "") ? "    Add description\n" : "") + ((this.newCategory == "") ? "    Add category\n" : "");
      alert(str)
    },
    changeOrder() {
      switch(this.orderTitle) {
        case "Ordered by title":
          this.orderTitle = "Ordered by category";
          break;
        case "Ordered by category":
          this.orderTitle = "Ordered by date";
          break;
        case "Ordered by date":
          this.orderTitle = "Ordered by title";
          break;
        default:
          this.orderTitle = "Ordered by title";
          break;
      }
    },
    moment
  },
  mounted() {
    this.getBlogs();
  },
  watch: {
    orderTitle() {
      switch(this.orderTitle) {
        case "Ordered by title":
          this.blogs.sort((blog1, blog2) => {
            let titleA = blog1.title.toUpperCase();
            let titleB = blog2.title.toUpperCase();
            
            if(titleA < titleB) {
              return -1;
            } else if(titleA > titleB) {
              return 1;
            } else {
              return 0;
            }
          });
          break;
        case "Ordered by category":
          this.blogs.sort((blog1, blog2) => {
            let catA = blog1.category.toUpperCase();
            let catB = blog2.category.toUpperCase();
            
            if(catA < catB) {
              return -1;
            } else if(catA > catB) {
              return 1;
            } else {
              return 0;
            }
          });
          break;
        case "Ordered by date":
          this.blogs.sort((blog1, blog2) => {
            return this.moment(blog1.date, "YYYY-MM-DD").toDate() - this.moment(blog2.date, "YYYY-MM-DD").toDate();
          });
          break;
      }
    }
  }
};
</script>
