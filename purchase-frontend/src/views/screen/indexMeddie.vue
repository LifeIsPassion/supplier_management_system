<template>
    <div class="echart">
        <div class="echartMain" ref="echartCk" style="height:688px;width:904px;margin-left: 0px;"></div>
    </div>
</template>

<script>

//mounted()  与 methods ： 中 ()与：的区别
import 'echarts/map/js/china'  // 引入中国地图
import {jsons} from '@/components/china.js'
export default {

    data() {
        return {
            //注销后就是后端查
            data: [
            ],
            //地图
            dataDepository:[],
            geoCoordMap: {
            },
        }
    },
    mounted() {
        // console.log("地图数据", jsons)
        this.getDepositoryToEChart()

    },
    methods: {

//逻辑问题
        convertData(data) {
            // console.log("geoCoordMap",this.geoCoordMap);
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
                backgroundColor: '#404a59',  //#04033a
                backgroundColor: '#404a59',
                title: {
                    text: '全国仓库数量可视化地图',
                  //  subtext: 'data from https://www.haidilao.com/',
                  //  sublink: 'https://www.haidilao.com/',
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
                        color: '#fff',
                    },
                },
                geo: {
                    map: 'china',
                    label: {
                        emphasis: {
                            show: false,
                        },
                    },
                    roam: true,
                    itemStyle: {
                        normal: {
                            areaColor: '#323c48',
                            borderColor: '#111',
                        },
                        emphasis: {
                            areaColor: '#2a333d',
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
                                color: '#ddb926',
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
     async   getDepositoryToEChart(){
            this.$http.get("/depository/getDepositoryToEChart").then((res)=>{
                // console.log("仓库地址：",res.data.data)
                this.data = res.data.data;
                this.dataDepository =res.data.data;
                // console.log("地图",res)
                if (res.data.code ==200) {
                    this.echartMapInit();
                }

                // console.log("调用后端：",this.data);
            });
        },

    }
}
</script>
<style lang='less' scoped>

</style>
