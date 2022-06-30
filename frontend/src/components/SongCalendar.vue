<template>
  <div class="mt-2 mx-4 mb-4">
    <ui-datepicker
      v-model="date"
      :config="config"
      placeholder="Select Month.."
      :month-options="monthOptions"
    ></ui-datepicker>
    <div class="grid grid-cols-7 gap-4">
      <div
        v-for="weekday in weekdays"
        v-bind:key="weekday"
        class="flex justify-start font-semibold"
      >
        {{ weekday }}
      </div>
    </div>
    <div class="grid grid-cols-7 gap-4">
      <div v-for="n in 42" v-bind:key="n">
        <ui-card
          v-if="isValidDay(n)"
          @click="
            open = true;
            selectedDay = n - getFirstWeekDayOfThisMonth();
          "
        >
          <ui-card-content class="bg-black">
            <ui-card-media>
              <ui-card-media-content class="text-white">{{
                n - getFirstWeekDayOfThisMonth()
              }}</ui-card-media-content>
            </ui-card-media>
            <ui-card-text></ui-card-text>
          </ui-card-content>
          <ui-card-content
            :class="
              n - getFirstWeekDayOfThisMonth() == getTodayDay() &&
              compareDates(date)
                ? 'h-16 bg-gray-600 cursor-pointer h-20'
                : 'h-16 bg-white cursor-pointer h-20'
            "
          >
            <ui-card-media>
              <ui-card-media-content>
                <div v-for="(item, index) in items" v-bind:key="index">
                  <div v-if="index == n - getFirstWeekDayOfThisMonth()">
                    <div
                      v-for="(singleItem, index) in item"
                      v-bind:key="singleItem"
                      :class="
                        n - getFirstWeekDayOfThisMonth() == getTodayDay() &&
                        compareDates(date)
                          ? 'px-1 text-white text-left cursor-pointer'
                          : 'px-1 text-black text-left cursor-pointer'
                      "
                    >
                      <div v-if="index < 2" class="truncate">
                        • {{ singleItem.artist }} - {{ singleItem.title }} ({{
                          singleItem.version
                        }})
                      </div>
                      <div v-else class="text-center font-bold">
                        {{item.length - 2}} more
                      </div>
                    </div>
                  </div>
                </div>
              </ui-card-media-content>
            </ui-card-media>
            <ui-card-text></ui-card-text>
          </ui-card-content>
        </ui-card>
        <ui-card v-else-if="n < getLastShownDay()">
          <ui-card-content class="bg-gray-200">
            <ui-card-media>
              <ui-card-media-content></ui-card-media-content>
            </ui-card-media>
            <ui-card-text></ui-card-text>
          </ui-card-content>
          <ui-card-content class="h-16 bg-gray-200">
            <ui-card-media>
              <ui-card-media-content></ui-card-media-content>
            </ui-card-media>
            <ui-card-text></ui-card-text>
          </ui-card-content>
        </ui-card>
      </div>
    </div>
    <!-- Simple dialog -->
    <ui-dialog v-model="open" scrollable resetScroll>
      <ui-dialog-title
        >Songs finished on {{ getSelectedDate() }}</ui-dialog-title
      >
      <ui-dialog-content>
        <ui-list>
          <div v-for="(item, index) in items" v-bind:key="index">
            <div v-if="index == selectedDay">
              <ui-item
                v-for="singleItem in item"
                v-bind:key="singleItem"
                @click="navigate(`/song/${singleItem.id}`)"
              >
                <div class="truncate">
                  • {{ singleItem.artist }} - {{ singleItem.title }} ({{
                    singleItem.version
                  }})
                </div></ui-item
              >
            </div>
          </div>
        </ui-list>
      </ui-dialog-content>
      <ui-dialog-actions>
        <ui-button @click="open = false">Cancel</ui-button></ui-dialog-actions
      >
    </ui-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      open: false,
      selectedDay: 0,
      date: "",
      realdate: "",
      config: {
        defaultDate: "today",
        mode: "month",
      },
      monthOptions: {
        shorthand: true, // defaults to false
        dateFormat: "M Y",
      },
      // https://developer.mozilla.org/de/docs/Web/JavaScript/Reference/Global_Objects/Date/toLocaleDateString
      dateOptions: { weekday: "short" },
      fullDateOptions: { day: "2-digit", month: "2-digit", year: "numeric" },
      monthYearOptions: { month: "short", year: "numeric" },
      // https://abbreviations.yourdictionary.com/articles/standard-month-and-days-of-the-week-abbreviations.html
      // https://www.timeanddate.com/calendar/days/monday.html
      weekdays: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
      months: [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
      ],
      showInvalid: true,
      items: [],
    };
  },
  mounted() {
    document.getElementById("content-main").style.backgroundColor = "#fff";
  },
  watch: {
    date() {
      if (typeof this.date == "string") {
        this.realdate = new Date(
          this.date.split(" ")[1],
          this.months.indexOf(this.date.split(" ")[0])
        );
      }
      var year = new Date(this.realdate).getFullYear();
      var month = new Date(this.realdate).getMonth() + 1;
      if (!isNaN(year) && !isNaN(month)) {
        fetch(`http://localhost:8080/api/song/calendar/${year}/${month}`)
          .then((response) => response.json())
          .then((json) => {
            this.items = json;
            console.log(this.items);
          })
          .catch(() => {});
      }
    },
  },
  created() {
    this.date = new Date();
    this.realdate = new Date();
  },
  methods: {
    navigate(route) {
      this.$router.push(route);
    },
    getSelectedDate() {
      // https://stackoverflow.com/questions/122102/what-is-the-most-efficient-way-to-deep-clone-an-object-in-javascript
      // Objekt muss geklont werden, um das Datum nicht global in der Datei zu ändern
      var selecteddate = new Date(JSON.parse(JSON.stringify(this.realdate)));
      selecteddate.setDate(this.selectedDay);
      return selecteddate.toLocaleDateString("en", this.fullDateOptions);
    },
    getLastDayOfLastMonth() {},
    getFirstWeekDayOfThisMonth() {
      return this.weekdays.indexOf(
        new Date(this.realdate).toLocaleDateString("en", this.dateOptions)
      );
    },
    getTodayDay() {
      return String(new Date().getDate());
    },
    compareDates(date) {
      return date == new Date().toLocaleDateString("en", this.monthYearOptions);
    },
    getLastDayOfThisMonth() {
      // https://stackoverflow.com/questions/222309/calculate-last-day-of-month
      return (
        new Date(
          this.realdate.getFullYear(),
          this.realdate.getMonth() + 1,
          0
        ).getUTCDate() + 1
      );
    },
    getLastShownDay() {
      var ret =
        this.getLastDayOfThisMonth() + this.getFirstWeekDayOfThisMonth();
      while (
        ret ==
          this.getLastDayOfThisMonth() + this.getFirstWeekDayOfThisMonth() ||
        ret % 7 != 1
      ) {
        ret++;
      }
      return ret;
    },
    isValidDay(day) {
      return (
        day - this.getFirstWeekDayOfThisMonth() > 0 &&
        this.getLastDayOfThisMonth() >= day - this.getFirstWeekDayOfThisMonth()
      );
    },
  },
};
</script>
