<template>
  <div class="echart">
    <div class="echartMain" ref="echartCk" style="height:688px;width:904px;margin-left: 0px;"></div>
  </div>
</template>

<script>

import 'echarts/map/js/china'  // 引入中国地图
import {jsons} from '@/components/china.js'
export default {

  data() {
    return {
      data: [
      ],
      //地图
      dataSupplier:[],
      geoCoordMap: {
      },
    }
  },
  mounted() {
    this.getSupplierToEChart()
  },
  methods: {

//逻辑问题
    convertData(data) {
      var res = [];
      for (var i = 0; i < data.length; i++) {
        var  data1= data[i].name;
        var d=  jsons[data[i].name]
        console.log("地图：",d);
        //后端与前端k不完全一样 可以前俩个字符串截取比较
        // let pattern = "/w"+data1.substring(0,2)+"/i";
        // let pattern1 = data1.substring(0,2);
        //         console.log(pattern);
        //   let result =  this.geoCoordMap.match(pattern1);
        //   console.log("模糊查询",result);
        // var geoCoord = this.geoCoordMap[data[i].name];
        var geoCoord = jsons[data[i].name];
        console.log("geoCoord",geoCoord)
        if (geoCoord) {
          res.push({
            name: data[i].name,
            value: geoCoord.concat(data[i].value),    //坐标
          });
        }
      }
      return res;
    },
    echartMapInit() {
      var map = this.$echarts.init(this.$refs.echartCk);
      var optionMap={
        title: {
          text: '供应商位置分布图',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: '28',
          },
        },
        tooltip: {
          trigger: 'item',
        },
        visualMap: {
          show: true,
          min: 0,
          max: 100,
          left: 'left',
          top: 'bottom',
          text: ['高', '低'],
          textStyle: {
            color: '#fff',
          },
          calculable: true,
          seriesIndex: [0],
          inRange: {
            color: ['#A5CC82', '#ffc0cb'],
          },
        },
        legend: {
          orient: 'vertical',
          y: 'bottom',
          x: 'right',
          data: ['haidilao'],
          textStyle: {
            color: '#004b75',
          },
        },
        geo: {
          map: 'china',
          aspectScale: 0.85,
          zoom: 1.2,
          top: '9%',//***********重点**********距离顶部的位置，每层向下一个百分比
          left: '11%',
          z: 5,
          label: {
            emphasis: {
              show: false,
            },
          },
          roam: false,
          itemStyle: {
            normal: {
              areaColor:{
                // areaColor: '#004b75',
                borderColor: '#195175',
                borderWidth: 2,
                shadowColor: '#0f4c74',
                shadowOffsetX: 0,
                shadowOffsetY: 4,
                shadowBlur: 10,
              },
              borderColor: '#111',
            },
            emphasis: {
              areaColor: '#004b75',
            },
          },
        },
        series: [
          {
            name: '地理位置+数量',
            type: 'scatter',
            coordinateSystem: 'geo',
            data: this.convertData(this.data),
            symbolSize: function (val) {
              return val[2] / 2;
            },
            label: {
              normal: {
                formatter: '{b}',
                position: 'right',
              },
              emphasis: {
                show: true,
              },
            },
            itemStyle: {
              normal: {
                areaColor:{
                  // type:'image',
                  // image:domImg
                  normal: {
                    // areaColor: '#323c48',
                    borderColor: '#111',
                    areaColor:{
                      areaColor: '#004b75',
                      borderColor: '#195175',
                      borderWidth: 2,
                      shadowColor: '#0f4c74',
                      shadowOffsetX: 0,
                      shadowOffsetY: 4,
                      shadowBlur: 10,
                    },
                  },
                },
              },
            },
          },
          {
            name: '',
            type: 'effectScatter',
            coordinateSystem: 'geo',
            data: this.convertData( this.data.sort(function (a, b) {
                return b.value - a.value;
              }).slice(0, 10)
            ),
            symbolSize: function (val) {
              return val[2] / 3;
            },
            showEffectOn: 'render',
            rippleEffect: {
              brushType: 'stroke',
            },
            hoverAnimation: true,
            label: {
              normal: {
                formatter: '{b}',
                position: 'right',
                show: true,
              },
            },
            itemStyle: {
              normal: {
                color: '#f4e925',
                shadowBlur: 10,
                shadowColor: '#333',
              },
            },
            zlevel: 1,
          },
        ],
      }
      map.setOption(optionMap);
    },

    // 获取地图信息
    async   getSupplierToEChart(){
      this.$http.get("/supplier/getSupplierToEchart").then((res)=>{
        this.data = res.data.data;
        this.dataSupplier =res.data.data;
        if (res.data.code ==200) {
          this.echartMapInit();
        }

        console.log("调用后端：",this.data);
      });
    },

  }
}
</script>
<style lang='less' scoped>

</style>
