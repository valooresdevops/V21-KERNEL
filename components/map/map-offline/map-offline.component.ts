import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChild,
  ElementRef,
  Input,
  ComponentFactoryResolver,
  ViewContainerRef,
  TemplateRef,
  Renderer2,
  Output,
  EventEmitter,
  HostListener,
} from '@angular/core';
import { Feature, View } from 'ol/index.js';
import Map from 'ol/Map';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { Draw, Modify, Snap,DragBox } from 'ol/interaction';
import { Heatmap, Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import { Circle as CircleStyle, Fill, Icon, Stroke, Style } from 'ol/style';
import { fromLonLat, useGeographic } from 'ol/proj.js';
import { Cluster } from 'ol/source.js';
import Point from 'ol/geom/Point.js';
import { Text } from 'ol/style.js';
import { transformExtent } from 'ol/proj';
import { HttpClient } from '@angular/common/http';
import { FullScreen, defaults as defaultControls } from 'ol/control.js';
import Swal from 'sweetalert2';
import * as ol from 'ol';
import Select from 'ol/interaction/Select';
import { altKeyOnly, click, pointerMove } from 'ol/events/condition.js';
import { remove } from 'ol/array';
import Overlay from 'ol/Overlay';
import * as $ from 'jquery';
import OSM from 'ol/source/OSM.js';
import {
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogModule,
  MatDialogConfig,
} from '@angular/material/dialog';
import { DatacrowdService } from '../Services/datacrowd.service';
import { DatePipe } from '@angular/common';
import { getArea, getLength } from 'ol/sphere.js';
import { Circle, Geometry, Polygon } from 'ol/geom';
// import { features } from 'process';
import GeoJSON from 'ol/format/GeoJSON';
import circle from 'ol/geom/Circle';
import { transform } from 'ol/proj';
import Polyline from 'ol/format/Polyline.js';
import LineString from 'ol/geom/LineString.js';
import { getPointResolution } from 'ol/proj';
import { unByKey } from 'ol/Observable.js';
import MultiPoint from 'ol/geom/MultiPoint.js';
// import { DataService } from '../data.service';
// import { LayerControlComponent } from '../components/layer-control/layer-control.component';
import 'jquery-ui';
import 'jqueryui';
import { tile } from 'ol/loadingstrategy';
import { createXYZ } from 'ol/tilegrid.js';
import 'jquery-ui';
import 'jqueryui';
// import { olext } from 'ol-ext';
// import { getVectorContext } from 'ol/render.js';
// import Path from 'ol-ext/featureAnimation/Path';
// import { size } from 'underscore';
// import { faSortAlphaAsc } from '@fortawesome/free-solid-svg-icons';
import * as turf from '@turf/turf';
import {platformModifierKeyOnly} from 'ol/events/condition.js';
import {getWidth} from 'ol/extent.js';
 import { empty, from, interval, lastValueFrom, Observable, of, Subscriber, Subscription, switchMap } from "rxjs";
  import {getRenderPixel} from 'ol/render.js';
  import MouseWheelZoom from 'ol/interaction/MouseWheelZoom.js';
  import DragAndDrop from 'ol/interaction/DragAndDrop.js';
// import { AnimationStyleMetadata } from '@angular/animations';
import { LayerControlComponent } from '../component/layer-control/layer-control.component';
import { DataService } from 'src/app/Kernel/services/data.service';
import { LoaderService } from 'src/app/Kernel/services/loader.service';
import { OfflinedataService } from '../Services/offlinedata.service';
import { VAgGridComponent } from '../component/v-ag-grid/v-ag-grid.component';
 


useGeographic();

const selected = new Style({
  fill: new Fill({
    color: '#eeeeee',
  }),
  stroke: new Stroke({
    color: 'rgba(255, 255, 255, 0.7)',
    width: 2,
  }),
});
function selectStyle(feature: any) {
  const color = feature.get('COLOR') || '#eeeeee';
  selected.getFill().setColor(color);
  return selected;
}
const select = new Select({
  condition: (event) => {
    return event.originalEvent.button === 1; // Right-click condition
  },
});

const container = document.getElementById('popup')!;

const overlay = new Overlay({
  element: container,
  autoPan: {
    animation: {
      duration: 250,
    },
  },
});

@Component({
  selector: 'mapOffline',
  templateUrl: './map-offline.component.html',
  styleUrls: ['./map-offline.component.css'],
})
export class MapOfflineComponent implements OnInit, AfterViewInit {
  @ViewChild(LayerControlComponent) layerpopupcomp!: LayerControlComponent;
  @ViewChild('saveMarker') saveMarker: any;
  @ViewChild('locationMarker') locationMarker: any;
  @ViewChild('SenarioContent') SenarioContent: any;
  @Output() senarioIdOutput:any= new EventEmitter<any>();
  @Input() changes:any;
  navbarSimulId:any;

  // Coord: Array<Coords> = [];
  reportType: any;
  reportTypeId: any;
  TimeZone: any;
  RecipientUser: any;
  DateTimeFrom: any;
  RecipientEmail: any;
  meter: any;
  Devices: any;
  isCSVAttached: any;
  dataType: any;
  telephoneNumber: any;
  EDGEHEIGHT: any;
  simulationid: any;
  usercode: any;
  centerCoordinates: any;
  radius!: number;
  isSimul: any;
  isFixedElements: any;
  FixedElementsPopup: boolean = false;
  countrycode:any;
  isAOi: any;
  multiselection: any;
 
  deletingModeEnabled: boolean = false;
  A_locSimulId: any;
  reportName: any;
  mapLayer: any;
  shapeName: any;
  // loading$ = this.loaderService.loading$;
  html1: any;
  shapeId: any;
  saveCheckbox: any;
  shapesArray: any[] = [];
  shapesDataArray: Array<any> = [];
  geoJsonData: any;
  lon: any;
  lat: any;
  vectorLayer: any;
  features: any;
  nameshapevalue: any;
  afternameshapevalue: any;

  locationName: any;
  cRdaius: any;
  fixedMarkersArray: any[] = [];
  ExecutionParam: any;
  ObjectID: any;
  fixedElementMarker: any;
  fixedMarkersGroup: any;
  bulkdraw: boolean = false;
  circleLayer: any;
  circleLayerLoop: any;
  polyline: any;
  polylinelayer: any;
  grid: boolean = false;
  GridPosition: any;
  points: any;
  _checked = this.data._checked;
  eventShapeId: any;
  shapeNamepopup: any;
  shapepopID: any;
  elementshape: any;
  shapeMeasure!: VectorLayer<any>;
  shapeNameTooltipElement: any;
  shapeNameTooltip!: any;
  sourcePolygon: any;
  sourcePolygonLoop: any;
  layerPolygon: any;
  layerPolygonLoop: any;
  layerPolyline: any;
  layerPolylineLoop: any;
  sourcePolyline: any;
  sourcePolylineLoop: any;
  overlayshapename: any[] = [];
  graphtools: any;
  moretools: any;
  editMode: boolean = false;
  modify: any;
  feature: any;
  select: any;
  isFixedElementsall: any;
  clickListener: any;
  cancelbtn: any;
  selectedStartDate: any;
  selectedEndDate: any;
  featuretarget: any;
  DatingObj: Array<any> = [];
  heats: any[] = [];
  heatmapSource!: VectorSource<any>;
  heatmapSources: any;
  clusterFeaturesHeat: any;
  nameshapeparameters: any[] = [];

  drawShapes: any[] = [];
 

  titlepopupname: any;

  valuepopupname: any;
  display: boolean = false;
  isBtsall: boolean = false;
  isBTS: boolean = false;
  clusters2: any;
  marker2: any;
  clusterFeatures2: any[] = [];
  clusterSource2: any;
  source2: any;
  dblclick: any;
  foundShape: any;
  olpopup: any;
  lastOverlay: any;
  nameshapevalue2: boolean = true;
  olpopupDate: any;
  shapeIdDate: any;
  overlayshapedate: any;
  dateshapeparameters: any;
  measurepopup: boolean = false;
  deleteMode: boolean = false;

  drawShape: boolean = false;
  selectspeificshape: any;
  measureIdArray: any[] = [];
  timelimit: any;
  mapLayoutType: any;
  lastSimulationDesc: any;
  overlaymeasure: any[] = [];
  BulkshapeId = 0;
  length: any;
  areaInSquareMeter: any;
  modifyol_uid: any;
  modifyfeature: any;
  displaymodified: any[] = [];
  modifydisplay: any;
  heatmapLayer: any;

  displayshapes: any[] = [];
  //url:string="https://10.1.8.23:443/openstreetmap/{z}/{x}/{y}.png";
  originalFeature: any;
  modifiedFeature: any;
  firstshape: any;
  modifiedshapes: any[] = [];
  originalGeometries: any = {};

  firstshapeid: any;
  firstmodifiedshapes: any[] = [];
  reportId: any;
  beginOperation: any;
  endOperation: any;
  deviceIdArr: any;
  deviceCoordinatesArr: any;
  cotravelermarker: any;
  comarkers: any;
  routeFeature!: Feature;
  geoMarker!: Feature;
  startMarker!: Feature;
  endMarker!: Feature;

  speedInput: any;
  startButton: any;
  route: any;
  position: any;
  // styles:any
  vectorLayeranimation: any;
  routeCoords: any;
  routeLength: any;
  now: any;
  styles: any;
  distance: any = 0;
  lastTime: any;
  speed: any;
  layer: any;
  LastSimualtionID: any;
  cotraveler: boolean = false;
  rowDatacotraveler: any[] = [];

  points1: any[] = [];
  msgEl: any;
  urlOsrmNearest = '//router.project-osrm.org/nearest/v1/driving/';
  urlOsrmRoute = '//router.project-osrm.org/route/v1/driving/';
  iconUrl = '//cdn.rawgit.com/openlayers/ol3/master/examples/data/icon.png';
  // vectorSource4: VectorSource<Geometry>;
  // vectorLayer4: VectorLayer<VectorSource<Geometry>>;
  vectorSource4: VectorSource<any>;
  vectorLayer4: VectorLayer<any>;
  fixedBtsArray: any[] = [];
  clusterSourcebts: any;
  clusterSourcebtsLoop: any;
  sourcebts: any;
  sourcebtsLoop: any;
  fixedbtsGroup: any;
  fixedbtsGroupLoop: any;
  bts: boolean = false;
  SectorMeter: number=100 ;
  fixedbtsGroupArray: any[] = [];
  fixedbtsGroupArrayLoop: any[] = [];
  devicesArray: any[] = [];
  speedTime: any = 1;
  isRunning: boolean = false;
  featurestcd: any;
  indexTimeline: number = 0;
  startTime: any;
  selectedTime: number = 0;
  ShowTimeline: boolean = false;
  ImgFirstCoord:any;
  Fixedsectorarray: Array<any> = [];
  FixedsectorarrayLoop: Array<any> = [];
  sectorobject: any;
  vectorLayerSector: any;
  vectorLayerSectorLoop: any;
  CdrData: any;
  featuresSector: any[]=[];
  CdrRowData: any;
  isTcd: boolean = false;
  openTable: boolean = false;
  Datatable: any[] = [];
  Datatablereverse:any[]=[];
  Datatable1:any[]=[];
  Datatable2:any[]=[];
  index:number=0;
  displayedColumns: any;
  Lng: any[] = [];
  latitude: any[] = [];
  event: any[] = [];
  Time: any[] = [];
  clusterFeaturesdev: any[] = [];
  maptypes:any;
  sectorResetArray:any[]=[];
  regiondata:any;
  coordinatepolygons:any[]=[];
  vectorSourceCountry:any;
  countries:any[]=[];
  typeElement:any;
  elementName:any;
  searchlat:any;
  searchlng:any;
  searchelement:boolean=false;
  devaddgrparray:any[]=[];
  hitsaddgrpnb:any[]=[];
  removesectors:any[]=[];
  displaysectors:boolean=false;
  displayclusters:boolean=false;
  tablerow:number=1;
  showbarstart:boolean=false;
  showbarreverse:boolean=false;
  senarioFlag:boolean=false;
  senariocount:number=0;
  senarioParentName:any;
  firstsenario:any;
  internalcode:any;
  addnewsenariocount:number=0;
  SenarioRowData:any;
  isGreen: boolean = true;
  isSqlCond: any;
  BTSObject: any;
  SenarioContentdiv:boolean=false;
  coTravelerId: any;
  DisplayCoTravelerflag: any;
  counter: number=0;
  checkCoTravelerCounter: number=0;
  coTravelerSubscription: any;
  
  coRelationId :any;
  checkCoRelationCounter :any;
  coRelationSubscription! :Subscription;
  DisplayCoRelationflag :number =0;
  aoiId :any;
  LNGArr: any;
  LATArr: any;
  AoiResultCoordArr: any;
  AOICircleRadius = 100;
  checkAoiCounter! :number;
  aoiSubscription!: Subscription;
  shapeIdArr: any;
  markeraoi:any;
  clusterFeaturesaoi:any[]=[];
  clusterSourceaoi:any;
  clustersaoi:any;
  sourceaoi:any;
  isRunningRoute:boolean=false;
  selectedDeviceId:any;
  routingpolyline:any[]=[];
  routingicons:any[]=[];
  animatedmarker1:any;
  animatedmarker1Loop:any;
  routingMarkerArr:any={};
  routingMarkerArrLoop:any={};
  typeofdata:any;
  routeDeviceArray:any[]=[];
  routeDeviceArrayLoop:any[]=[];
  routeDevicestable:any[]=[];
  routecountobj:number=0;
  layers:any[]=[];
  openMagnifier:boolean=false;
  polylineRouteArray:any[]=[];
  xroutearray:any[]=[];
  speedTimeRoute: any = 0;
  routedatajson: any;
  displayRoute:boolean=false;
  displayroute:boolean=false;
  routeDevices:any;
  indexRoute: number = 0;
  count:number=0;
  inQueueTiming: any;
  SectorColor: any;
  SectorFillColor: any;
  currentSectorColor: any;
  Senario_reportType: any;
  cursorlnglat:any;
  isDimmed:boolean=false;
  zoommagnifymap: number=16;
  routePointAnal:any[]=[];
  routeolylineAnal:any[]=[];
  routePointAnalLoop:any[]=[];
  routeolylineAnalLoop:any[]=[];
  fixedBtsArrayLoop:any[]=[];
  FixedsectorarrayLoopLayer:any[]=[];
  ShowHeader:boolean=false;
  showTextMenu: boolean = false;
  nextActionMenuList:any[]=[];
  selectedType: number = 1; 
  dateTimeFrom:any;
  dateTimeTo:any;
  Convertedsimulationtype:any;
  simulationtype: any;
  deviceValue: string = '';
  IMSI_IDValue: string = '';
  records: any;

   constructor(
    private datacrowdService: DatacrowdService,
    private httpClient: HttpClient,
    private componentFactoryResolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef,
    public dialog: MatDialog,
    private dataservice: DatacrowdService,
    private dataservice1: DataService,
    private datePipe: DatePipe,
    public loaderService: LoaderService,
    public data: OfflinedataService,
    private renderer: Renderer2,
    private el: ElementRef
  ) {
    this.vectorSource4 = new VectorSource();
    this.vectorLayer4 = new VectorLayer({
      source: this.vectorSource4,
    });
    this.styles = {
      route: new Style({
        stroke: new Stroke({
          width: 6,
          color: 'yellow',
        }),
      }),
      icon: new Style({
        image: new Icon({
          anchor: [0.5, 1],
          src: this.iconUrl,
        }),
      }),
    };
  }
  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef;
  @ViewChild('popup-content') popup: any;
  @ViewChild('deleteMeasurebtn', { static: false })
  deleteMeasurebtn!: ElementRef;
  // @ViewChild('bulkdraw') bulkdraw: any;

  public shapenamearray: Array<{
    IDshape: number;
    Nameshape: String;
    PolyBoundsCoordsshape: Array<any>;
  }> = [];

  public coordinatesArray: Array<{
    Bounds: Array<{ lng: number; lat: number }> | string;
    ID: any;
    Name: String;
    PolyBoundsCoords: Array<{ lng: number; lat: number }> | string;
    Type: string;
    Value: string;
    center: { lng: number; lat: number };
    ol_uid: any;
    radius: any;
    selectedStartDate: any;
    selectedEndDate: any;
    leafletid: any;
    countrycodes:any
  }> = [];

  clusters: any;
  clustersLoop: any;
  manysimularray:any[]=[];
  manysimularrayLoop:any[]=[];
  private map!: Map;
  private magnifiedMap!: Map;
  datajson: any;
  // clusterFeatures:any[] = [];
  clusterFeatures: Array<any> = [];
  clusterFeaturesLoop: Array<any> = [];
  lng: any;

  coordinates: any;
  coordinates1: any;
  clusterSource: any;
  clusterSourceLoop: any;
  source: any;
  sourceLoop: any;
  selectedFeatureID: number = 0;
  featureID: number = 0;
  isSelectMode = false;
  myMarker: any;
  Html2: any;
  value: boolean = false;
  fixedelementname:any;
  fixedelementpopup:boolean=false;
  childInput: string = '';
  receivedData: any;
  DateTimeTo: any;
  radiusInMeters: any;
  overlay: any;
  zoom: any;
  globalCoord: any;
  toFixedValue: any;
  polygoneLimit: any;
  circleLimit: any;
  squareLimit: any;
  mapUrl!: any;
  url!: string;
  ip: any;
  marker: any;
  circleFeature: any;
  circleFeatureLoop: any;
  title: String = 'Here On';
  fixedMarkersGroupsArray: any[] = [];
  circles: any[] = [];
  circlesLoop: any[] = [];
  polygons: any[] = [];
  polygonsLoop: any[] = [];
  polylines: any[] = [];
  polylinesLoop: any[] = [];
  uniqueNames: any[] = [];
  AoiIds: any;
  Aoiresp: any;
  draw: any;
  isdatepicker: boolean = false;

  sketch: any;
  helpTooltipElement: any;
  helpTooltip: any;
  measureTooltipElement: any;
  measureTooltip: any;
  continuePolygonMsg = 'Click to continue drawing the polygon';
  continueLineMsg = 'Click to continue drawing the line';
  listener: any;
  snap: any;
  F_name: any;
  F_Type: any;
  measureId: any;
  clusters2Array: any[] = [];
  ImsiID: any;
  animating: boolean = false;
  center = [-5639523.95, -3501274.52];
  olpopupOverlay: any;
  markerDev: any;
  sourcedev: any;
  clustersourcedev: any;
  clustersdev: any;
  aggridmarker: boolean = false;
  animationSpeed: number = 1;
  animationInterval: any;
  polylineCoordsRoute: any;
  currentIndex: number = 0;
  markerFeature: any;
  GridID:any;
  BtsTypeSlected: string = '';

  //// measure functions :
  measure(shape: any) {
    this.addInteraction(shape);
    this.deactivateDrawInteraction();
  }

  //calculate polyline
  formatLength = (line: any) => {
    const length = getLength(line) * 100000;

    let output;
    output = Math.round(length * 100) / 100 + 'm';
    // }
    return output;
  };

  //calculate polygon
  formatArea = (polygon: any) => {
    const area = getArea(polygon) * 10000;

    let output;
    output = Math.round(area * 100000000) / 100 + 'm<sup>2</sup>';

    return output;
  };

  //to draw polyline or polgon
  addInteraction(shape: any) {
    this.deactivateDrawInteraction();
    this.source = new VectorSource();
    this.deactivateDrawInteraction();
    // const type =   elect.value == 'area' ? 'Polygon' : 'LineString';
    const type = shape;
    this.draw = new Draw({
      source: this.source,
      type: type,
      style: new Style({
        fill: new Fill({
          color: '#D0F0C0',
        }),
        stroke: new Stroke({
          color: '#008000',
          width: 2,
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: '#7eb2fb',
          }),
        }),
      }),
    });
    this.map.addInteraction(this.draw);
    console.log('measurecoordinaeeeeee:', this.coordinatesArray);
    this.createMeasureTooltip(this.measureId);
    // this.createHelpTooltip();

    this.draw.on('drawstart', (evt: any) => {
      // this.deactivateDrawInteraction();

      // set sketch
      this.sketch = evt.feature;

      /** @type {import("../src/ol/coordinate.js").Coordinate|undefined} */
      let tooltipCoord = evt.coordinate;

      this.listener = this.sketch.getGeometry().on('change', (evt: any) => {
        const geom = evt.target;
        console.log("geom-------------",geom);
        let output;
        if (geom instanceof Polygon) {
          output = this.formatArea(geom);
          tooltipCoord = geom.getInteriorPoint().getCoordinates();
        } else if (geom instanceof LineString) {
          output = this.formatLength(geom);
          tooltipCoord = geom.getLastCoordinate();
        }
        this.measureTooltipElement.innerHTML = output;

        this.measureTooltip.setPosition(tooltipCoord);
      });
    });

    this.draw.on('drawend', (feature: any) => {
      this.measureId = feature.feature['ol_uid'];
      this.overlaymeasure.push({
        id: this.measureId,
        element: this.measureTooltipElement,
      });
      this.measureTooltipElement.id = `measure_${this.measureId}`;

      this.measureIdArray.push(this.measureId);
      this.deactivateDrawInteraction();
      this.map.removeInteraction(this.draw);

      this.measureTooltipElement.className = 'ol-tooltip ol-tooltip-static';
      this.measureTooltip.setOffset([0, -7]);

      this.sketch = null;

      this.measureTooltipElement = null;

      if (this.shapeMeasure) {
        this.shapeMeasure = new VectorLayer({
          source: this.source,
          style: new Style({
            fill: new Fill({
              color: '#D0F0C040',
            }),
            stroke: new Stroke({
              color: '#008000',
              width: 2,
            }),
            image: new CircleStyle({
              radius: 7,
              fill: new Fill({
                color: '#7eb2fb',
              }),
            }),
          }),
        });
        this.shapesArray.push(this.shapeMeasure);
      } else {
        this.shapeMeasure = new VectorLayer({
          source: this.source,
          style: new Style({
            fill: new Fill({
              color: '#D0F0C040',
            }),
            stroke: new Stroke({
              color: '#008000',
              width: 2,
            }),
            image: new CircleStyle({
              radius: 7,
              fill: new Fill({
                color: '#7eb2fb',
              }),
            }),
          }),
        });
        this.shapesArray.push(this.shapeMeasure);
      }
      this.map.addLayer(this.shapeMeasure);
      unByKey(this.listener);
      this.deactivateDrawInteraction();
    });
    this.deactivateDrawInteraction();
    this.map.addInteraction(select);
  }
  ///popup for measure
  createMeasureTooltip(measureId: any) {
    if (this.measureTooltipElement) {
      this.measureTooltipElement.parentNode.removeChild(
        this.measureTooltipElement
      );
    }
    this.measureTooltipElement = document.createElement('div');
    this.measureTooltipElement.setAttribute('id', `measure_${measureId}`);
    this.measureTooltipElement.className =
      'ol-tooltip ol-tooltip-measure measuredynamic';
    this.measureTooltip = new Overlay({
      element: this.measureTooltipElement,
      offset: [0, -15],
      positioning: 'bottom-center',
      stopEvent: false,
      insertFirst: false,
    });
    this.map.addOverlay(this.measureTooltip);
  }

  private _pointerMoveHandler = (evt: any) => {
    if (evt.dragging) {
      return;
    }
    let helpMsg = 'Click to start drawing';

    if (this.sketch) {
      const geom = this.sketch.getGeometry();
      if (geom instanceof Polygon) {
        helpMsg = this.continuePolygonMsg;
      } else if (geom instanceof LineString) {
        helpMsg = this.continueLineMsg;
      }
    }
  };

  // add heats on map
  addHeatmapLayer() {
    const heatmapData = [
      [0, 0, 0.5], // [longitude, latitude, intensity]
      [10, 10, 1.0],
      // Add more data points as needed
    ];

    const vectorSource = new VectorSource({
      features: heatmapData.map((data) => {
        const feature = new Feature({
          geometry: new Point(fromLonLat([data[0], data[1]])),
        });
        feature.set('weight', data[2]);
        return feature;
      }),
    });

    this.heatmapLayer = new Heatmap({
      source: vectorSource,
      blur: 10,
      radius: 10,
      weight: 'weight',
    });

    this.map.addLayer(this.heatmapLayer);
  }

  GridType1: string = 'btn-54';
  GridType2: string = 'opentcd';
  selectedFeatures: any;
  feature_onClick!: any;
  rowData = [{ Device_id: ' ', Date: ' ', Coord: ' ', Hits: ' ' }];
  rowDatabts = [
    {
      Type: ' ',
      Location: ' ',
      BTSName: ' ',
      Sector: ' ',
      Frequency: ' ',
      Lng: ' ',
      Lat: ' ',
      Azimuth: ' ',
    },
  ];
  TcdRowData: any = [
    {
      imsi_id: '',
      imei_id: '',
      service_provider_id: '',
      usage_timeframe: '',
      location_latitude: '',
      location_longitude: '',
      cgi_id: '',
      location_azimuth: '',
      type_id: '',
      phone_number: '',
      Technology: '',
    },
  ];
  sector2rowdata: any;
  tcd2: boolean = false;
  autoGroupColumnDef = {
    headerName: 'athlete',
    field: 'athlete',
    minWidth: 400,

    cellRendererParams: {
      checkbox: true,
    },
  };
  autoGroupColumnDeftcd = {
    headerName: 'Technology',
    field: 'Technology',
    minWidth: 200,
  };

  autoGroupColumnDefsSenario ={
    // enabled sorting on Row Group Columns only 
    sortable: true,      
    headerName: 'Organisational Hierarchy',
    cellRendererParams: {
      innerRenderer: 'group',
      suppressCount: true,
      expandedByDefault: false
  
    },
    minWidth: 300,
    sort: 'asc',
      comparator: function (valueA:any, valueB:any, nodeA:any, nodeB:any, isInverted:any) {
        // Compare ids of two rows
        const idA = nodeA.data.id;
        const idB = nodeB.data.id;
        
        // Return comparison result
        if (idA < idB) {
          return -1;
        } else if (idA > idB) {
          return 1;
        } else {
          return 0;
        }
      },
    cellClassRules: {
  'parent-row': function(params:any) {
    // Check if the row has children
    console.log('params.node',params.node.childrenAfterSort.length > 0 && params.node.childrenAfterGroup.length > 0 && params.node.childrenAfterFilter.length > 0)
    return  params.node.childrenAfterSort.length > 0 && params.node.childrenAfterGroup.length > 0 && params.node.childrenAfterFilter.length > 0;
  },
  'current-row': (params:any) =>{
    // Check if the row has children
    console.log('params<>><<>',params.data.id)
    return params.data.id==this.simulationid;
  },
}
    
    
  
  
    
  };
  SenariocolumnDefs = [
    {
      headerName: 'Senario',
      field: 'orgHierarchy',
      minWidth: 500,
      cellRenderer: 'agGroupCellRenderer',
      // checkboxSelection: true,
      hide:true,
      sort: 'asc',
      comparator: function (valueA:any, valueB:any, nodeA:any, nodeB:any, isInverted:any) {
        // Compare ids of two rows
        const idA = nodeA.data.id;
        const idB = nodeB.data.id;
        
        // Return comparison result
        if (idA < idB) {
          return -1;
        } else if (idA > idB) {
          return 1;
        } else {
          return 0;
        }
      },
    cellClassRules: {
  'parent-row': function(params:any) {
    // Check if the row has children
    console.log('params.node',params.node.childrenAfterSort.length > 0 && params.node.childrenAfterGroup.length > 0 && params.node.childrenAfterFilter.length > 0)
    return  params.node.childrenAfterSort.length > 0 && params.node.childrenAfterGroup.length > 0 && params.node.childrenAfterFilter.length > 0;
  },
  'current-row': (params:any) =>{
    // Check if the row has children
    console.log('params<>><<>',params.data.id)
    return params.data.id==this.simulationid;
  },
}

    }
  ];
  columnDefsTcd = [
    {
      headerName: 'Technology',
      field: 'Technology',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      width: 200,
      rowGroup: true,
      hide: true,
    },
    {
      headerName: 'imsi_id',
      field: 'imsi_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
    },
    {
      headerName: 'imei_id',
      field: 'imei_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
      width: 250,
    },
    {
      headerName: 'service_provider_id',
      field: 'service_provider_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
      width: 250,
    },
    {
      headerName: 'usage_timeframe',
      field: 'usage_timeframe',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
    },
    {
      headerName: 'location_latitude',
      field: 'location_latitude',
      sortable: true,
      filter: 'agSetColumnFilter',
      width: 400,
      wrapText: true,
      autoHeight: true,
      autoSizeColumns: true,
      padding: 0,
      //sort: 'desc',
    },
    {
      headerName: 'location_longitude',
      field: 'location_longitude',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
    },

    {
      headerName: 'cgi_id',
      field: 'cgi_id',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      width: 100,
    },
    {
      headerName: 'location_azimuth',
      field: 'location_azimuth',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      width: 100,
    },
    {
      headerName: 'type_id',
      field: 'type_id',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      width: 200,
    },
    {
      headerName: 'phone_number',
      field: 'phone_number',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      width: 200,
    },
  ];
  columnDefsTcd2 = [
    {
      headerName: 'imsi_id',
      field: 'imsi_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
    },
    {
      headerName: 'imei_id',
      field: 'imei_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
      // width: 250,
    },
    {
      headerName: 'service_provider_id',
      field: 'service_provider_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
      //  width: 250,
    },
    {
      headerName: 'usage_timeframe',
      field: 'usage_timeframe',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
    },
    {
      headerName: 'location_latitude',
      field: 'location_latitude',
      sortable: true,
      filter: 'agSetColumnFilter',
      // width: 400,
      wrapText: true,
      autoHeight: true,
      autoSizeColumns: true,
      padding: 0,
      //sort: 'desc',
    },
    {
      headerName: 'location_longitude',
      field: 'location_longitude',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
    },

    {
      headerName: 'cgi_id',
      field: 'cgi_id',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      // width: 100,
    },
    {
      headerName: 'location_azimuth',
      field: 'location_azimuth',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      // width: 100,
    },
    {
      headerName: 'type_id',
      field: 'type_id',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      // width: 200,
    },
    {
      headerName: 'phone_number',
      field: 'phone_number',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      // width: 200,
    },
    {
      headerName: 'Technology',
      field: 'Technology',
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: '',
      padding: 0,
      // width: 200,
    },
  ];

  columnDefs = [
    {
      headerName: 'Device_id',
      field: 'Device_id',
      sortable: true,
      filter: 'agSetColumnFilter',
      // width:500,
      autoHeight: true,
      rowGroup: true,
      padding: 0,
      left: 0,
      hide: true,
    },
    {
      headerName: 'Date',
      field: 'Date',
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      padding: 0,
      width: 300,
    },
    {
      headerName: 'Coord',
      field: 'Coord',
      sortable: true,
      filter: 'agSetColumnFilter',
      width: 150,
      autoSizeColumns: true,
      cellStyle: { 'text-align': 'right', color: 'blue' },
    },
  ];

  columnDefs2 = [

    {
      headerName: "Device_id",
      field: "Device_id",
      sortable: true,
      filter: "agSetColumnFilter",
      width: 400,
      wrapText: true,
      autoHeight: true,
 
      cellStyle: { flex: '1' },
    },
    {
      headerName: "Date",
      field: "Date",
      sortable: true,
      filter: "agSetColumnFilter",
      autoSizeColumns: true,
      sort: 'asc',
      wrapText: true,
      cellStyle: { "text-align": "-webkit-center", "flex": '1' },
      //cellRenderer: 'btnCellRenderer',
      // cellRendererParams: {
      //   clicked: function(field: any) {
      //     alert(`${field} was clicked`);
      //   },
      // },
      valueGetter: (params: any) => {
        if (typeof (params.data) != "undefined") {
          return this.dateTtoDate(params.data.Date);

        }
      }
    },
    {
      headerName: "Coord",
      field: "Coord",
      sortable: true,
      filter: "agSetColumnFilter",
      autoSizeColumns: true,
      padding: 0,
      // sort: 'desc',
      wrapText: true,
      cellStyle: { "text-align": "-webkit-center", "flex": '1' },
    },
    {
      headerName: "Hits",
      field: "Hits",
      sortable: false,
      filter: false,
      autoSizeColumns: true,
      width: 100,
      valueGetter: function (params: any) {
        var count = 0;
        params.api.forEachNode(function (node: any) {
          if (node.data.Device_id === params.data.Device_id) {
            count++;
          }
        });
        return count;
      },
      cellStyle: { "text-align": "-webkit-center", "flex": '1' },
    },

  ];

  columnDefsBts: any[] = [
    {
      headerName: 'Type',
      field: 'Type',
      sortable: true,
      filter: 'agSetColumnFilter',
      width: 400,
      wrapText: true,
      autoHeight: true,
      cellStyle: { flex: '1' },
    },
    {
      headerName: 'Location',
      field: 'Location',
      sortable: true,
      width: 400,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      cellStyle: { 
        // 'text-align': 'right', 
        color: 'blue' },
    },
    {
      headerName: 'BTSName',
      field: 'BTSName',
      width: 400,
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      // cellStyle: { 'text-align': 'left' },
    },
    {
      headerName: 'Sector',
      field: 'Sector',
      sortable: false,
      filter: false,
      autoSizeColumns: true,
      width: 400,
      cellStyle: { 'text-align': '-webkit-center', flex: '1' },
    },
    {
      headerName: 'Frequency',
      field: 'Frequency',
      width: 400,
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      // cellStyle: { 'text-align': 'right' },
    },
    {
      headerName: 'Lng',
      field: 'Lng',
      width: 400,
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      // cellStyle: { 'text-align': 'right' },
    },
    {
      headerName: 'Lat',
      field: 'Lat',
      width: 400,
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      // cellStyle: { 'text-align': 'right' },
    },
    {
      headerName: 'Azimuth',
      field: 'Azimuth',
      width: 400,
      sortable: true,
      filter: 'agSetColumnFilter',
      autoSizeColumns: true,
      // cellStyle: { 'text-align': 'right' },
    },
  ];

  @ViewChild(VAgGridComponent) agGrid!: VAgGridComponent;
  @ViewChild('popup') pop!: TemplateRef<any>;

  public vectorSource = new VectorSource({ wrapX: false });

  public darwLayer = new VectorLayer({
    source: this.vectorSource,
    style: new Style({
      fill: new Fill({
        color: '#7eb2fb40',
      }),
      stroke: new Stroke({
        color: '#007FFF',
        width: 3,
      }),
      image: new CircleStyle({
        radius: 7,
        fill: new Fill({
          color: '#7eb2fb',
        }),
      }),
    }),
  });

  private drawInteraction: any;
 

  status: boolean = false;
  clickEvent() {
    this.status = !this.status;
  }
  toggleButtonContainer() {
    const buttonContainer = document.querySelector('.button-container');
    buttonContainer?.classList.toggle('hide');
  }

  elem_id(id: any) {
    return document.getElementById(id);
  }
  view = new ol.View({
    center: [36, 34],
    zoom: 9,
    // minZoom: 5,   
     maxZoom: 18 
  });

  async ngOnInit() {
    $('#popup11111').css('display', 'none');
    $('#popup2').css('display', 'none');
    $('#popupGrid').css('display', 'none');
    $('#popup-closer').css('display', 'none');
    $('#TCDContent').css('display', 'none');  
    $('#fixedelementsPopup').css('display', 'none');  
    $('#datepicker').css('display', 'none');  
    $('#cotravelerpopup').css('display', 'none');  
    $('#fixedelementpopup').css('display', 'none');  

    this.datacrowdService.getmaptypesOffline().then((res) => {
      this.maptypes=res;
      console.log('maptypesssss------------',res);
    });


    this.datacrowdService.getgraphtools().then((res) => {
      this.datacrowdService.getgraphtools().then((res) => {
      this.graphtools = res;
      console.log('this.graphtools', this.graphtools);
    });});

    this.datacrowdService.getmoretools().then((res) => {
      this.moretools = res;
      console.log('moretools >>>>>>>>>>>>>>>>>', this.moretools);
    });

    await this.datacrowdService.getSimulationTypes().then((res: any) => {
      this.simulationtype = res;
      console.log('simulationtype >>>>>>>>>>>>>>>>>', this.simulationtype);
      this.Convertedsimulationtype=this.convertArray(this.simulationtype);
      console.log('Convertedsimulationtype >>>>>>>>>>>>>>>>>', this.Convertedsimulationtype);

    });

      this.datacrowdService.getShapelimit().then((res) => {

      console.log('getShapelimit>>', res);
      const myArray: any = res;
      for (const obj of myArray) {
        const [id, property, value] = obj;
        console.log('getShapelimit>>><<<', `${property}: ${value}`);
 
        const myArray:any =res;
         
 
        this.polygoneLimit=myArray[0][1];
        this.circleLimit=myArray[0][2];
        this.squareLimit=myArray[0][3];
        this.timelimit=myArray[0][4];
        this.mapLayoutType=myArray[0][5];
        this.globalCoord=myArray[0][6];
        this.zoom=myArray[0][7];
        this.ip=myArray[0][8];

        this.inQueueTiming=myArray[0][9];

        this.lastSimulationDesc = myArray[0][10];
        this.SectorMeter = myArray[0][11];
        this.SectorColor = myArray[0][12];
        this.SectorFillColor = myArray[0][13];
        this.currentSectorColor = myArray[0][14];
  
        // this.maxzoom=
        console.log("polygoneLimit>>>>>>>>>>>>",this.polygoneLimit);
        console.log("circleLimit>>>>>>>>>>>>",this.circleLimit);
        console.log("squareLimit>>>>>>>>>>>>",this.squareLimit);
        console.log("urllllllllllllll>>>>>>>>>>>>",this.ip);
      }
    });
    if (this.simulationtype && this.simulationtype.length > 0) {
      this.selectedType = this.simulationtype[0][0]; // Selecting the value of the first option
    }

    if (localStorage.getItem('userCode')) {
      if (localStorage.getItem('locSimulId')) {
        // this.displaycaseSimulation();
      }
    }
 
    const offlineSource = new XYZ({
      // url:"https://10.1.7.30/tileserver-php-master/lebsatellite1/{z}/{x}/{y}.png",
      // url:"https://10.1.7.30/tileserver-php-master/lebsatellite1/{z}/{x}/{y}.png",
    //  url:"https://10.10.10.60/tileserver-php-master/satellite/{z}/{x}/{y}.png",
       // url: "https://10.1.8.97:443/openstreetmap/{z}/{x}/{y}.png",
     // url: "https://10.1.8.23:443/openstreetmaplebanon/{z}/{x}/{y}.png",
      //url: "https://10.1.7.24:443/map/{z}/{x}/{y}.png",
      // url: 'https://10.1.8.23:443/openstreetmaplebanon/{z}/{x}/{y}.png',
      url:"https://10.1.2.205/tileserver-php-master/satellite/{z}/{x}/{y}.png",

      minZoom: 0,
      maxZoom: 15,
      });

      this.mapLayer = new TileLayer({
        source: offlineSource,
      });
 
      
  const vectorSource11 = new VectorSource({
    format: new GeoJSON(),
    url: function (extent) {
      return (
        'https://ahocevar.com/geoserver/wfs?service=WFS&' +
        'version=1.1.0&request=GetFeature&typename=osm:water_areas&' +
        'outputFormat=application/json&srsname=EPSG:4326&' +
        'bbox=' +
        extent.join(',') +
        ',EPSG:4326'  
      );
    },
    strategy: tile(createXYZ({tileSize: 512})),
  });
 
  const vector1111 = new VectorLayer({
    source: vectorSource11,
    style: new Style({
      stroke: new Stroke({
        color: 'rgba(0, 0, 255, 1.0)',
        width: 2,
      }),
    }),
  });
 
  var olview = new ol.View({
    center: [-5483805.05941004,-1884105.3933610613],
    zoom: 16,
    minZoom: 2,
    maxZoom: 20
  });
  let magnifiedMap:any = document.getElementById('magnifying-glass1');

   let container:any = document.getElementById('mapContainer');
    this.map = new Map({
      controls: defaultControls().extend([
        new FullScreen({
          source: 'fullscreen',
        }),
      ]),
      layers: [this.mapLayer, this.darwLayer/*, this.clusters*/,/* vector1111*/],
      overlays: [overlay],
      target: container,
      view: this.view,

    });
    // const offlineSourceLoop = new XYZ({
    //   url:"https://10.1.2.205/tileserver-php-master/satellite/{z}/{x}/{y}.png",
    //     minZoom: 0,
    //     maxZoom:20,
    //   });

    //    let mapLayerLoop = new TileLayer({
    //     source: offlineSourceLoop,
    //   });

    // var map1 = new ol.Map({
    //   layers: [ mapLayerLoop  ],
    //   target: 'magnifying-glass1',
    //   view: new ol.View({
    //     center:  transform([10, 45], 'EPSG:4326', 'EPSG:3857'),
    //     zoom: 22
    //   })
    // });


    // console.log(' map1.getTarget()---', map1.getTarget())
    // map1.setTarget( map1.getTarget());
    // map1.on(('click'),()=>{
    //   alert('click');
    // })


    const roadLayer = new TileLayer({
      source: new XYZ({
    
        url:"https://10.1.2.205/tileserver-php-master/satellite/{z}/{x}/{y}.png",
        minZoom: 0,
        maxZoom: 15,
      }),
    });
    
 
    const view = new View({
      center: [33, 35],
      zoom: 13,
    });
    
   this.magnifiedMap = new Map({
      target: 'magnifying-glass1',
      layers: [roadLayer],
      view: view,
    });
    
   
  
    console.log("magnnnnnn", $(".magnifying-glass1 .ol-viewport"));
    $(".magnifying-glass1 .ol-viewport").css('border-radius', '50%' );
  
    $('.magnifying-glass1').css('display', 'none');

    this.map.on('pointermove', (event) => {
//  console.log("event===",event.coordinate);

      const type = this.map.hasFeatureAtPixel(event.pixel)
        ? 'pointer'
        : 'inherit';
      this.map.getViewport().style.cursor = type;

      if(this.openMagnifier==true){
        this.cursorlnglat = event.coordinate;
        this.updateMagnifyingGlass(event);
        }

    });
    
    this.changeInteraction();

    this.map.addEventListener('contextmenu', (event) => {
      event.preventDefault();
    });

    this.map.on('pointermove', (event) => {

      const coordinate = this.map.getEventCoordinate(event.originalEvent);
      
      const lat = coordinate[1].toFixed(6);
      const lng = coordinate[0].toFixed(6);
      const mouseCoordDiv = document.getElementById('MouseCoord');
      mouseCoordDiv!.innerHTML = `${lat} : ${lng}`;
    });

    
    let mousePosition:any = null;
    container.addEventListener('mousemove',   (event:any) =>{
 
      mousePosition = this.map.getEventPixel(event);
      this.map.render();
    });
    
    container.addEventListener('mouseout',   () =>{
      mousePosition = null;
      this.map.render();
    });
    
    

   

    const dblClickHandler = (evt: any) => {
      if (this.nameshapevalue === true) {
      } else {
        //alert("namingshpeeeeeeeeeeeeeeeee")
        this.dblclick = 0;
        console.log('evtttttttttttt:::::::::', evt);
        this.foundShape = false;
        let foundfeature: any;
        // Check for features at the clicked location
        this.map.forEachFeatureAtPixel(evt.pixel, (feature) => {
          // Do something with the clicked feature
          if (feature) {
            this.foundShape = true;
            foundfeature = feature;
            this.dblclick = 1;
          }
        });
        let idint = parseInt(foundfeature.getProperties().geometry.ol_uid) + 1;
        let idoluil = idint.toString();

        const foundItem = this.coordinatesArray.find((a) => a.ID === idoluil);

        if (this.foundShape && foundItem != undefined) {
          //alert("foundnamingshapeeee")

          this.nameshapevalue = true;
          let shape = true;

          if (foundItem) {
            // alert("inside if foundshape")
            console.log('foundItem::::::::::::::::::', foundItem['Name']);
            if (foundItem['Name'] != '') {
              this.valuepopupname = '';
              this.nameshapevalue = true;
              this.titlepopupname = 'Re-enter shape name:';
              this.valuepopupname = foundItem['Name'];
            } else if (foundItem['Name'] === '') {
              this.valuepopupname = '';
              this.nameshapevalue = true;
              this.titlepopupname = 'Enter shape name:';
              this.valuepopupname = foundItem['Name'];
            } else if (foundItem['Name'] === 'undefined') {
              // alert('undefineddddddddd')
            }
          }

          var coord = this.map.getCoordinateFromPixel(evt.pixel);
          this.createOverlayNamingShape(coord);
          let intid =
            parseInt(foundfeature.getProperties().geometry.ol_uid) + 1;
          let id = intid.toString();
          let result = this.coordinatesArray.find((a) => a.ID === id);
          var resultName = result!['Name'];
          console.log('id:::::', id, '     Name:::::::', resultName);

          if (resultName === '') {
            this.foundShape = false;
            this.data.ok.subscribe((name: string) => {
              if (shape) {
                // this.olpopup = null;
                console.log('name >>>>> ', name);
                this.closepopupnameshape(foundfeature);
              }
              shape = false;
            });
          } else {
            // this.olpopup = null;
            this.data.ok.subscribe((name: string) => {
              this.foundShape = false;

              console.log('idddd:::', id);
              if (shape) {
                this.deleteOverlayById(id);
                console.log('overlaynamearrayyyyyy::::', this.overlayshapename);
                console.log('name >>>>> ', name);
                this.closepopupnameshape(foundfeature);
              }
              shape = false;
            });
          }
          this.data.cancel.subscribe((name: string) => {
            // this.olpopup = null;
            this.foundShape = false;
            if (shape) {
              console.log('name >>>>> ', name);
              this.closepopupnameshapeCancel();
            }
            shape = false;
          });
        }
        this.foundShape = false;
        this.dblclick = +1;
      }
    };

    this.map.on('dblclick', dblClickHandler);

    //// to handle the click of each shape(open the datepicker popup)
    this.map.getViewport().addEventListener('contextmenu', (evt: any) => {
      if (this.isdatepicker === true) {
      } else {
        let foundShape = false;
        let foundfeature: any;

        evt.preventDefault();
        console.log('evt>>>>>>>>>>>>>>>>>>>>>', evt);

        let AzimuthSelected = '';
        let lngSelcted = '';
        let latSelected = '';
        let TechnologySelected = '';

        const pixel = [evt.clientX, evt.clientY];
        const coordinate = this.map.getCoordinateFromPixel(pixel);
        let features: any = this.map.getFeaturesAtPixel(pixel);

        console.log('features in right click -----', features);

        console.log('evt in right click -----', evt);
        if (features.length > 0) {
          // Assuming you only have one feature at this pixel
          let feature = features[0];
          console.log('feature[0]-----', feature);
          console.log('values_-----', feature['values_']);
          console.log('values_-----', feature['values_'].geometry.flatCoordinates);
          let coordelement=feature['values_'].geometry.flatCoordinates;
          console.log('values_-----',coordelement);

          console.log(
            'getGeometry-----',
            feature['values_'].geometry.constructor.name
          );
          console.log(
            'Right-clicked feature type:',
            feature.getGeometry().getType()
          );
          let geometrytype: any = feature.getGeometry().getType();
          if (
            this.aggridmarker === false &&
            (geometrytype === 'Circle' ||
              geometrytype === 'Polygon' ||
              geometrytype === 'LineString')
          ) {
            // Get the geometry of the feature
            let geometry = parseInt(feature.getGeometry().ol_uid) + 1;
            let id = geometry.toString();

            console.log('geometry:::::::::::::::::::::::::::::::', id);
            // Check if the geometry is a circle
            if (this.coordinatesArray.find((a) => a.ID === id)) {
              // Check if the clicked feature is the circle
              if (features && features.length > 0) {
                if (this.deleteMode === false) {
                  this.isdatepicker = true;
                  foundShape = true;
                  this.featuretarget = features[0].geometryChangeKey_.target;
                  const result = this.findDataById(
                    parseInt(this.featuretarget.ol_uid) + 1
                  );
                  this.shapeIdDate = (
                    parseInt(this.featuretarget.ol_uid) + 1
                  ).toString();
                  this.dataservice1.setshapeIdDate(this.shapeIdDate);

                  this.value = false;
                  $('#datepicker').css('display', '');

                  //  console.log(`properties >>>>>>>>>>>>>>>>`, properties);
                  var popup: any = this.elem_id('datepicker');
                  var popup_closer = this.elem_id('popup-closer')!;
                  var popup_content = this.elem_id('popup-content');
                  var olpopup: any = new ol.Overlay({
                    element: popup!,
                    autoPan: false,
                  });

                  if (result) {
                    this.dataservice1.setStartDate(result.selectedStartDate);
                    this.dataservice1.setendDate(result.selectedEndDate);
                  } else {
                    this.dataservice1.setStartDate('');
                    this.dataservice1.setendDate('');
                  }

                  var coord = [
                    this.featuretarget.flatCoordinates[0],
                    this.featuretarget.flatCoordinates[1],
                  ];
                  //  var coord = this.map.getCoordinateFromPixel(evt.pixel);
                  olpopup.setPosition(coord);
                  this.map.addOverlay(olpopup);
                  //  $('#datepicker').css('display', '');

                  $(document).ready(() => {
                    $('#alertify-ok').click(async () => {
                      if (foundShape) {
                        console.log(
                          '>>>>>>>>><<<<<<<<<<<<<getdtaaaaaaaaaaaaaaaaaaaa',
                          this.dataservice1.getData()
                        );

                        let data = await this.dataservice1.getData();
                        this.selectedStartDate = data.selectedStartDate;
                        this.selectedEndDate = data.selectedEndDate;

                        let dateshapename =
                          'Begin Operation:' +
                          this.selectedStartDate +
                          '<br>End Operation:' +
                          this.selectedEndDate;
                        let x = {
                          id: parseInt(this.featuretarget.ol_uid) + 1,
                          selectedStartDate: this.selectedStartDate,
                          selectedEndDate: this.selectedEndDate,
                        };
                        if (
                          typeof this.selectedStartDate == 'undefined' ||
                          this.selectedStartDate === '' ||
                          this.selectedEndDate === '' ||
                          typeof this.selectedEndDate == 'undefined'
                        ) {
                          // this.DatingObj.push(x);
                          let selectedStartDate = (this.coordinatesArray.find(
                            (a) => a.ID === this.shapeIdDate
                          )!['selectedStartDate'] = '');
                          let selectedEndDate = (this.coordinatesArray.find(
                            (a) => a.ID === this.shapeIdDate
                          )!['selectedEndDate'] = '');
                          this.deleteOverlayById(
                            parseInt(this.featuretarget.ol_uid) + 1
                          );
                          let x = {
                            id: parseInt(this.featuretarget.ol_uid) + 1,
                            selectedStartDate: selectedStartDate,
                            selectedEndDate: selectedEndDate,
                          };
                          this.DatingObj.push(x);
                          this.dataservice1.setStartDate(
                            this.selectedStartDate
                          );
                          this.dataservice1.setendDate(this.selectedEndDate);
                        } else {
                          this.DatingObj.push(x);
                          this.dataservice1.setStartDate(
                            this.selectedStartDate
                          );
                          this.dataservice1.setendDate(this.selectedEndDate);
                          this.dataservice1.setvaluedate(0);
                          this.createshapeNameTooltip(
                            dateshapename,
                            coord,
                            parseInt(this.featuretarget.ol_uid) + 1,
                            'dateTooltip'
                          );
                          const itemToEdit = this.coordinatesArray.find(
                            (item) =>
                              parseInt(item.ol_uid) ===
                              parseInt(this.featuretarget.ol_uid) + 1
                          );
                          if (itemToEdit) {
                            itemToEdit.selectedStartDate =
                              this.selectedStartDate;
                            itemToEdit.selectedEndDate = this.selectedEndDate;
                          } else {
                          }
                        }
                      }
                      foundShape = false;
                    });
                    $('#alertify-cancel').click(async () => {
                      this.closepopup();
                    });
                  });

                  popup_closer.onclick = () => {
                    olpopup.setPosition(undefined);

                    return false;
                  };
                  $('#popupGrid').css('display', 'none');
                  $('#popup2').css('display', 'none');
                }
              }
            } else {
              console.log('cannot click on this feauture .');
            }
          }

          let sectorType = feature['values_'].type;
          console.log("type2222222222--------",sectorType);
          if (sectorType === 'sector') {
            $('#TCDContent').css('display', '');
            console.log('feature >>>>>>>>>>>>>>>>', feature);
            console.log('Azimuth:', feature.get('Azimuth'));
            AzimuthSelected = feature.get('Azimuth');
            console.log('Lng:', feature.get('lng'));
            lngSelcted = feature.get('lng');
            console.log('Lat:', feature.get('lat'));
            latSelected = feature.get('lat');
            console.log('Technology:', feature.get('Technology'));
            TechnologySelected = feature.get('Technology');
            let SINFO = feature.get('SINFO');
            let SINFO2 = feature.get('SINFO2');
            console.log('SINFO----------', SINFO);
            console.log('SINFO22222222----------', SINFO2);
            let findedSectors: any;

            if (AzimuthSelected === '') {
              console.log('Azimuth is null');
              this.closeTCDPopup();
            } else {
              console.log('Azimuth not null ');
              if (SINFO != '') {
                findedSectors = SINFO.filter((element: any) => {
                  console.log('element----------', element);
                  // console.log('element[7]----------', element[7]);
                  // console.log('element[4]----------', element[4]);
                  // console.log('element[5]----------', element[5]);
                  // console.log('AzimuthSelected----------', AzimuthSelected);
                  // console.log('latSelected----------', latSelected);
                  // console.log('lngSelcted----------', lngSelcted);

                  return (
                    element[7] === AzimuthSelected.toString() &&
                    element[4] === latSelected.toString() &&
                    element[5] === lngSelcted.toString()
                  );
                });
                console.log('findedSectors when right click', findedSectors);
                this.TcdRowData = [];
                findedSectors.forEach((element: any, key: any) => {
                  var jsonaggrid = {
                    imsi_id: element[0],
                    imei_id: element[1],
                    service_provider_id: element[2],
                    usage_timeframe: this.dateTtoDate(element[3]),
                    location_latitude: element[4],
                    location_longitude: element[5],
                    cgi_id: element[6],
                    location_azimuth: element[7],
                    type_id: element[8],
                    phone_number: element[9],
                    Technology: element[10],
                  };
                  // console.log('jsonaggrid----------', jsonaggrid);

                  this.TcdRowData.push(jsonaggrid);
                });

                console.log('TcdRowData------------', this.TcdRowData);
                //// open popup
                var popup: any = this.elem_id('TCDContent');
                var popup_closer = this.elem_id('popup-closer')!;
                var popup_content = this.elem_id('popup-content');

                this.olpopupOverlay = new ol.Overlay({
                  element: popup,
                  autoPan: false,
                });

                this.olpopupOverlay.setPosition([lngSelcted, latSelected]);
                this.map.addOverlay(this.olpopupOverlay);
              } else if (SINFO2 != '') {
                this.bts = true;
                this.GridID='btspopup';
                $('#btspopup').css('display', '');

                findedSectors = SINFO2.filter((element: any) => {
                  console.log('element----------', element);
                  console.log('element[1]----------', element[1]);
                  console.log('element[3]----------', element[3]);
                  console.log('element[2]----------', element[2]);
                  return (
                    element[1] == AzimuthSelected.toString() &&
                    element[3] === latSelected.toString() &&
                    element[2] === lngSelcted.toString()
                  );
                });
                console.log('findedSectors when right click', findedSectors);
                this.rowDatabts = [];
                findedSectors.forEach((element: any, key: any) => {
                  var jsonaggrid = {
                    Type: 'Sector',
                    Location: element[5],
                    BTSName: element[9],
                    Sector: element[1],
                    Frequency: element[6],
                    Lng: element[2],
                    Lat: element[3],
                    Azimuth: element[1],
                  };
                  console.log('jsonaggrid--------', jsonaggrid);
                  this.rowDatabts.push(jsonaggrid);
                });
                this.columnDefsBts = this.columnDefsBts;

                console.log('rowDatabts', this.rowDatabts);
                //// open popup
                var popup: any = this.elem_id('btspopup');
                var popup_closer = this.elem_id('popup-closer')!;

                this.olpopupOverlay = new ol.Overlay({
                  element: popup,
                  autoPan: false,
                });

                this.olpopupOverlay.setPosition([lngSelcted, latSelected]);
                this.map.addOverlay(this.olpopupOverlay);
              }
            }
          }else if(sectorType === 'icon'){

            var popup: any = this.elem_id('elementpopup');
            var popup_closer = this.elem_id('popup-closer')!;
            var popup_content = this.elem_id('popup-content');
            var olpopup: any = new ol.Overlay({
              element: popup!,
              autoPan: false,
            });

            olpopup.setPosition(coordelement);
            this.map.addOverlay(olpopup);
          } else {
            // alert('else');

            console.log(
              'values_.features-----',
              feature['values_'].features[0]['values_']
            );
            console.log(
              'typeeeeeeeeeeeeeeeeee-----',
              feature['values_'].features[0]['values_'].type
            );
            let type = feature['values_'].features[0]['values_'].type;

            if (type === 'bts') {
              // alert('bts');
              this.GridID='btspopup';
              $('#btspopup').css('display', '');
              this.bts = true;
              $('#datepicker').css('display', 'none');

              this.deleteMode = false;

              console.log(`feature >>>>>>>>>>>>>>>>`, feature);
              const properties = feature.getProperties();
              console.log(
                `properties 1111111111111111111>>>>>>>>>>>>>>>>`,
                properties
              );
              console.log(
                `type 1111111111111111111>>>>>>>>>>>>>>>>`,
                properties.type
              );
              console.log(
                `flatCoordinates>>>>>>>>>>>>>>>>`,
                properties.geometry.flatCoordinates
              );
              if (properties.id) {
              } else if (properties.type === 'sector') {
                this.displaySectorGrid(properties);
              } else if (properties.features) {
                console.log(
                  `properties.id >>>>>>>>>>>>>>>>`,
                  properties.features.length
                );
                console.log(
                  `2222222222222222222222 >>>>>>>>>>>>>>>>`,
                  properties.features
                );

                for (let i = 0; i < properties.features.length; i++) {
                  console.log(
                    '4444444444444444444444------',
                    properties.features[i]['values_']['info']
                  );
                  console.log(
                    'Type------',
                    properties.features[i]['values_'].Type
                  );
                  let info = properties.features[i]['values_']['info'];
                  for (let j = 0; j < info.length; j++) {
                    this.rowDatabts.push({
                      Type: `${feature['values_'].features[0]['values_'].type}`,
                      Location: `${properties.features[i]['values_']['info'][j][5]}`,
                      BTSName: `${properties.features[i]['values_']['info'][j][9]}`,
                      Sector: `${properties.features[i]['values_']['info'][j][1]}`,
                      Frequency: `${properties.features[i]['values_']['info'][j][6]}`,
                      Lng: `${properties.features[i]['values_']['info'][j][3]}`,
                      Lat: `${properties.features[i]['values_']['info'][j][2]}`,
                      Azimuth: `${properties.features[i]['values_']['info'][j][1]}`,
                    });
                  }

                  console.log('rowDatabts:::::::::', this.rowDatabts);
                }
                this.columnDefsBts=this.columnDefsBts;
                // this.columnDefsBts = [
                //   {
                //     headerName: 'Type',
                //     field: 'Type',
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     width: 100,
                //     wrapText: true,
                //     autoHeight: true,
                //     cellStyle: { flex: '1' },
                //   },
                //   {
                //     headerName: 'Location',
                //     field: 'Location',
                //     sortable: true,
                //     width: 100,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right', color: 'blue' },
                //   },
                //   {
                //     headerName: 'BTSName',
                //     field: 'BTSName',
                //     width: 100,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                //   {
                //     headerName: 'Sector',
                //     field: 'Sector',
                //     sortable: false,
                //     filter: false,
                //     autoSizeColumns: true,
                //     width: 100,
                //     cellStyle: { 'text-align': '-webkit-center', flex: '1' },
                //   },
                //   {
                //     headerName: 'Frequency',
                //     field: 'Frequency',
                //     width: 100,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                //   {
                //     headerName: 'Lng',
                //     field: 'Lng',
                //     width: 100,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                //   {
                //     headerName: 'Lat',
                //     field: 'Lat',
                //     width: 100,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                //   {
                //     headerName: 'Azimuth',
                //     field: 'Azimuth',
                //     width: 100,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                // ];

                //// open popup
                console.log(`properties >>>>>>>>>>>>>>>>`, properties.features);
                var popup: any = this.elem_id('btspopup');
                var popup_closer = this.elem_id('popup-closerbts')!;

                var coord0 = properties.geometry.flatCoordinates[1];
                var coord1 = properties.geometry.flatCoordinates[0];
                this.olpopupOverlay = new ol.Overlay({
                  element: popup,
                  autoPan: false,
                });

                this.olpopupOverlay.setPosition([coord1, coord0]);
                this.map.addOverlay(this.olpopupOverlay);

                // this.openPopupp(event, olpopup);
                $('#popupGrid').css('display', 'none');
                $('#popup2').css('display', 'none');
                $('#cotravelerpopup').css('display', 'none');
                this.deactivateDrawInteraction();
              }
            } else if (type === 'marker') {
              this.aggridmarker = true;
              this.value=true;
              // alert('markerrrrrrrr');
              console.log(`feature >>>>>>>>>>>>>>>>`, feature);
              const properties = feature.getProperties();

              console.log(`properties >>>>>>>>>>>>>>>>`, properties);
              if (properties.id) {
              } else if (properties.features) {
                // datajs = [];
                this.rowData = [];

                console.log(
                  `properties.id >>>>>>>>>>>>>>>>`,
                  properties.features.length
                );
                this.GridID='popupGrid';
                for (let i = 0; i < properties.features.length; i++) {
                  this.rowData.push({
                    Device_id: `${properties.features[i]['values_']['info'][2]}`,
                    Date: `${new Date(
                      properties.features[i]['values_']['info'][3]
                    )}`,
                    Coord: `${properties.features[i]['values_']['info'][0]},${properties.features[i]['values_']['info'][1]}`,
                    Hits: `${properties.features[i]['values_']['info'][3]}`,
                  });
                }
                
                this.columnDefs2 = this.columnDefs2 ;
                // this.columnDefs2 = [
                //   {
                //     headerName: 'Device_id',
                //     field: 'Device_id',
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     width: 310,
                //     wrapText: true,
                //     autoHeight: true,
                //     cellStyle: { flex: '1' },
                //   },

                //   {
                //     headerName: 'Date',
                //     field: 'Date',
                //     width: 150,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                //   {
                //     headerName: 'Coord',
                //     field: 'Coord',
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     width: 150,
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right', color: 'blue' },
                //   },
                //   {
                //     headerName: 'Hits',
                //     field: 'Hits',
                //     sortable: false,
                //     filter: false,
                //     autoSizeColumns: true,
                //     width: 100,
                //     valueGetter: function (params: any) {
                //       var count = 0;
                //       params.api.forEachNode(function (node: any) {
                //         if (node.data.Device_id === params.data.Device_id) {
                //           count++;
                //         }
                //       });
                //       return count;
                //     },
                //     cellStyle: { 'text-align': '-webkit-center', flex: '1' },
                //   },
                // ];

                //// open popup
                console.log("columndefs:",this.columnDefs);
                console.log("rowdata:",this.rowData);
                console.log(`properties >>>>>>>>>>>>>>>>`, properties);
                var popup: any = this.elem_id('popup2');
                var popup_closer = this.elem_id('popup-closer')!;
                var popup_content = this.elem_id('popup-content');
                var olpopup: any = new ol.Overlay({
                  element: popup!,
                  autoPan: false,
                });
                console.log('geometry-------', properties['geometry']);

                console.log(
                  'flatCoordinates-------',
                  properties['geometry']['flatCoordinates']
                );

                let lng = properties['geometry']['flatCoordinates'][0];
                console.log('lng', lng);
                let lat = properties['geometry']['flatCoordinates'][1];
                console.log('lat', lat);
                this.GridPosition = [lng, lat];
                olpopup.setPosition(this.GridPosition);
                this.map.addOverlay(olpopup);
                console.log(`11111111111 >>>>>>>>>>>>>>>>`);

                $('#popup2').css('display', '');
                this.value = true;

                // popup_closer.onclick = () => {
                //   olpopup.setPosition(undefined);

                //   //  $("#popup2").hide();
                //   return false;
                // };
                console.log(`22222222222 >>>>>>>>>>>>>>>>`);

                // this.openPopupp(event, olpopup);
                console.log(`333333333333333 >>>>>>>>>>>>>>>>`);

                $('#popupGrid').css('display', 'none');
                this.deactivateDrawInteraction();
                console.log(`44444444444 >>>>>>>>>>>>>>>>`);
              }
            } else if (type === 'markerdev') {
              // alert('markerrrrrrrr');
              console.log(`feature >>>>>>>>>>>>>>>>`, feature);
              const properties = feature.getProperties();

              console.log(`properties >>>>>>>>>>>>>>>>`, properties);
              if (properties.id) {
              } else if (properties.features) {
                // datajs = [];
                this.rowData = [];

                console.log(
                  `properties.id >>>>>>>>>>>>>>>>`,
                  properties.features.length
                );
                for (let i = 0; i < properties.features.length; i++) {
                  console.log(
                    'info lengthhh-------',
                    properties.features[i]['values_']['info'].length
                  );
                  console.log(
                    'info ----------',
                    properties.features[i]['values_']['info']
                  );

                  let info = properties.features[i]['values_']['info'];

                  for (let j = 0; j < info.length; j++) {
                    console.log(
                      `info jjjjjjjjjj>>>>>>>>>>>>>>>>`,
                      `${info[j]}`
                    );

                    console.log(`Device_id>>>>>>>>>>>>>>>>`, `${info[j][2]}`);
                    console.log(`Tel>>>>>>>>>>>>>>>>`, `${info[j][4]}`);
                    console.log(
                      `Date>>>>>>>>>>>>>>>>`,
                      `${new Date(info[j][3])}`
                    );
                    console.log(`Hits>>>>>>>>>>>>>>>>`, `${info[j][3]}`);

                    this.rowData.push({
                      Device_id: `${info[j][2]}`,
                      Date: `${new Date(info[j][3])}`,
                      Coord: `${info[j][0]},${info[j][1]}`,
                      Hits: `${info[j][3]}`,
                    });
                  }
                }
                this.columnDefs2 = this.columnDefs2 ;
                // this.columnDefs2 = [
                //   {
                //     headerName: 'Device_id',
                //     field: 'Device_id',
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     width: 310,
                //     wrapText: true,
                //     autoHeight: true,
                //     cellStyle: { flex: '1' },
                //   },

                //   {
                //     headerName: 'Date',
                //     field: 'Date',
                //     width: 150,
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right' },
                //   },
                //   {
                //     headerName: 'Coord',
                //     field: 'Coord',
                //     sortable: true,
                //     filter: 'agSetColumnFilter',
                //     width: 150,
                //     autoSizeColumns: true,
                //     cellStyle: { 'text-align': 'right', color: 'blue' },
                //   },
                //   {
                //     headerName: 'Hits',
                //     field: 'Hits',
                //     sortable: false,
                //     filter: false,
                //     autoSizeColumns: true,
                //     width: 100,
                //     valueGetter: function (params: any) {
                //       var count = 0;
                //       params.api.forEachNode(function (node: any) {
                //         if (node.data.Device_id === params.data.Device_id) {
                //           count++;
                //         }
                //       });
                //       return count;
                //     },
                //     cellStyle: { 'text-align': '-webkit-center', flex: '1' },
                //   },
                // ];

                //// open popup
                console.log(`properties >>>>>>>>>>>>>>>>`, properties);
                var popup: any = this.elem_id('popup2');
                var popup_closer = this.elem_id('popup-closer')!;
                var popup_content = this.elem_id('popup-content');
                var olpopup: any = new ol.Overlay({
                  element: popup!,
                  autoPan: false,
                });
                console.log('geometry-------', properties['geometry']);

                console.log(
                  'flatCoordinates-------',
                  properties['geometry']['flatCoordinates']
                );

                let lng = properties['geometry']['flatCoordinates'][0];
                console.log('lng', lng);
                let lat = properties['geometry']['flatCoordinates'][1];
                console.log('lat', lat);
                this.GridPosition = [lng, lat];
                olpopup.setPosition(this.GridPosition);
                this.map.addOverlay(olpopup);
                console.log(`11111111111 >>>>>>>>>>>>>>>>`);

                $('#popup2').css('display', '');
                this.value = true;

                // popup_closer.onclick = () => {
                //   olpopup.setPosition(undefined);

                //   //  $("#popup2").hide();
                //   return false;
                // };
                console.log(`22222222222 >>>>>>>>>>>>>>>>`);

                // this.openPopupp(event, olpopup);
                console.log(`333333333333333 >>>>>>>>>>>>>>>>`);

                $('#popupGrid').css('display', 'none');
                this.deactivateDrawInteraction();
                console.log(`44444444444 >>>>>>>>>>>>>>>>`);
              }
            }
            else if(type === 'fixedelement'){
              this.fixedelementpopup=true;
              console.log(`feature >>>>>>>>>>>>>>>>`, feature);
              const properties = feature.getProperties();

              console.log(`properties >>>>>>>>>>>>>>>>`, properties);
      
              this.fixedelementname=properties.features[0].A_name;
              console.log(`fixedelementname >>>>>>>>>>>>>>>>`, this.fixedelementname);

              var popup: any = this.elem_id('fixedelementpopup');
     
              var olpopup: any = new ol.Overlay({
                element: popup!,
                autoPan: false,
              });
              console.log('geometry-------', properties['geometry']);

              console.log(
                'flatCoordinates-------',
                properties['geometry']['flatCoordinates']
              );

              let lng = properties['geometry']['flatCoordinates'][0];
              console.log('lng', lng);
              let lat = properties['geometry']['flatCoordinates'][1];
              console.log('lat', lat);
              this.GridPosition = [lng, lat];
              olpopup.setPosition(this.GridPosition);
              this.map.addOverlay(olpopup);
              console.log(`11111111111 >>>>>>>>>>>>>>>>`);

              $('#popup2').css('display', '');
              this.value = true;

              // popup_closer.onclick = () => {
              //   olpopup.setPosition(undefined);

              //   //  $("#popup2").hide();
              //   return false;
              // };
              console.log(`22222222222 >>>>>>>>>>>>>>>>`);

              // this.openPopupp(event, olpopup);
              console.log(`333333333333333 >>>>>>>>>>>>>>>>`);

              $('#popupGrid').css('display', 'none');
            }
          }
        }
      }
    });
  

      this.map.on('click', (event) => {

      let i = 0;

      const feature: any = this.map.forEachFeatureAtPixel(
        event.pixel,
        (feature) => {
          console.log(
            'feature.getGeometry()?.getType()::::',
            feature.getGeometry()?.getType()
          );
          console.log(
            'featureeeeeeeeeeeeeeeeeeeeeeeeee::::',
            feature.getProperties()
          );
          console.log('namefeatureeeeee::::', feature.getProperties());

          return feature;
        }
      );
      if (feature) {
        let type = feature.getGeometry()?.getType();
        if (type === 'Circle' || type === 'Polygon' || type === 'LineString') {
          dblClickHandler(event);
        } else if (type === 'Point') {
          feature.getProperties()['features'].forEach((feature: any) => {
            if (feature.values_.name === 'marker') {
              i = 1;
            } else {
            }
          });
          if (i === 1) {
            const coordinates = feature.getGeometry().getCoordinates();
            const zoomLevel = 20; // Change this to your desired zoom level
            this.map.getView().animate({
              center: coordinates,
              zoom: zoomLevel,
              duration: 1000, // Animation duration in milliseconds
            });
          }
        } else {
        }
      }
      if (this.openMagnifier) {
        
        $('.magnifying-glass1').css('display', '');
         let magnifyingGlass:any = document.getElementById('magnifying-glass1');

        const currentWidth = parseFloat(window.getComputedStyle(magnifyingGlass).width);
        const currentHeight = parseFloat(window.getComputedStyle(magnifyingGlass).height);
        console.log("currentWidth----", currentWidth, "---currentHeight---", currentHeight)
        magnifyingGlass.style.width = `${currentWidth + 0}px`;
        magnifyingGlass.style.height = `${currentHeight + 0}px`;
        this.updateMagnifyingGlassSize();
        this.updateMagnifyingGlass(event);
      }
    });
    $('#btspopup').css('display', 'none');

    /////////////////////////in animation
 

    function splitLineString(
      geometry: any,
      minSegmentLength: any,
      options: any
    ) {
      function calculatePointsDistance(coord1: any, coord2: any) {
        var dx = coord1[0] - coord2[0];
        var dy = coord1[1] - coord2[1];
        return Math.sqrt(dx * dx + dy * dy);
      }

      function calculateSplitPointCoords(
        startNode: any,
        nextNode: any,
        distanceBetweenNodes: any,
        distanceToSplitPoint: any
      ) {
        var d = distanceToSplitPoint / distanceBetweenNodes;
        var x = nextNode[0] + (startNode[0] - nextNode[0]) * d;
        var y = nextNode[1] + (startNode[1] - nextNode[1]) * d;
        return [x, y];
      }

      function calculateAngle(startNode: any, nextNode: any, alwaysUp: any) {
        var x = startNode[0] - nextNode[0];
        var y = startNode[1] - nextNode[1];
        var angle = Math.atan(x / y);
        if (!alwaysUp) {
          angle = y > 0 ? angle + Math.PI : x < 0 ? angle + Math.PI * 2 : angle;
        }
        return angle;
      }

      var splitPoints = [];
      var coords = geometry.getCoordinates();
      var coordIndex = 0;
      var startPoint = coords[coordIndex];
      var nextPoint = coords[coordIndex + 1];
      var angle = calculateAngle(startPoint, nextPoint, options.alwaysUp);

      var n = Math.ceil(geometry.getLength() / minSegmentLength);
      var segmentLength = geometry.getLength() / n;
      var currentSegmentLength = options.midPoints
        ? segmentLength / 2
        : segmentLength;

      for (var i = 0; i <= n; i++) {
        var distanceBetweenPoints = calculatePointsDistance(
          startPoint,
          nextPoint
        );
        currentSegmentLength += distanceBetweenPoints;

        if (currentSegmentLength < segmentLength) {
          coordIndex++;
          if (coordIndex < coords.length - 1) {
            startPoint = coords[coordIndex];
            nextPoint = coords[coordIndex + 1];
            angle = calculateAngle(startPoint, nextPoint, options.alwaysUp);
            i--;
            continue;
          } else {
            if (!options.midPoints) {
              var splitPointCoords = nextPoint;
              splitPointCoords.push(angle);
              splitPoints.push(splitPointCoords);
            }
            break;
          }
        } else {
          var distanceToSplitPoint = currentSegmentLength - segmentLength;
          var splitPointCoords: any = calculateSplitPointCoords(
            startPoint,
            nextPoint,
            distanceBetweenPoints,
            distanceToSplitPoint
          );
          startPoint = splitPointCoords.slice();
          splitPointCoords.push(angle);
          splitPoints.push(splitPointCoords);
          currentSegmentLength = 0;
        }
      }

      return splitPoints;
    }

    var route = new Polyline({
      factor: 1e6,
    }).readGeometry('polyline', {
      dataProjection: 'EPSG:4326',
      featureProjection: 'EPSG:4326',
    });

    let routeCoords: any;
    if (route instanceof LineString) {
      routeCoords = route.getCoordinates();
    }

    var routeLength = routeCoords.length;
    console.log('routeLength>>>>>>>>>>>>>>>>>>>>', routeLength);
    var routeFeature = new ol.Feature({
      type: 'route',
      geometry: route,
    });
    var startMarker = new ol.Feature({
      type: 'icon',
      geometry: new Point(routeCoords[0]),
    });
    var endMarker = new ol.Feature({
      type: 'icon',
      geometry: new Point(routeCoords[routeLength - 1]),
    });

    var repeat = false;
    var timer = -1;

    var style = function (feature: any, resolution: any) {
      if (feature.get('type') == 'route') {
        var styles = [
          new Style({
            stroke: new Stroke({
              width: 6,
              color: [237, 212, 0, 0.8],
            }),
          }),
        ];

        if (timer < 0) {
          // alert('timer<0');
          feature.unset('splitPoints', true);
        } else {
          // alert('timer>>>>>>>0');

          var splitPoints = feature.get('splitPoints');
          // console.log("points1111111111111111111111:",splitPoints)
          if (!splitPoints) {
            splitPoints = splitLineString(
              feature.getGeometry(),
              2 * resolution,
              { alwaysUp: false, midPoints: true }
            );
            // feature.set('splitPoints', splitPoints, true);
            //  console.log("splitPoints::::::",splitPoints);
            var y = feature.getGeometry().getCoordinates();
            //  console.log("feature.getGeometry()::::::",feature.getGeometry().getCoordinates());
          }
          if (!repeat && timer >= y.length) {
            stopAnimation(true);
          } else {
            var index = timer % y.length;
            // console.log("index::::::",index);
            var point = y[index];
            // console.log("point::::::",point);
            styles.push(
              new Style({
                geometry: new Point([point[0], point[1]]),
                image: new Icon({
                  src: ' /assets/assetsOffline/icons/singleperson.png',
                  rotation: point[2],
                }),
                zIndex: 1,
              })
            );
          }
        }
        return styles;
      } else {
        return [
          new Style({
            image: new Icon({
              anchor: [0.5, 1],
              src: '/assets/icons/SEpoint.png',
            }),
          }),
        ];
      }
    };

    var vector = new VectorLayer({
      source: new VectorSource({
        features: [routeFeature, startMarker, endMarker],
      }),
      style: style,
    });

    this.map.addLayer(vector);

    var animating = false;
    var token: any;
    var speedInput: any = document.getElementById('speed');
    var startButton: any = document.getElementById('start-animation');

    const startAnimation = () => {
      // alert('startAnimation');

      if (animating) {
        // alert('startAnimation   animating');
        stopAnimation(false);
      } else {
        // alert('startAnimation   elseeeeeeeeeeeeeeeeee');

        animating = true;
        startButton.textContent = 'Cancel Animation';
        token = setInterval(function () {
          timer++;
          // console.log("timerrrrrrr::::",timer);
          vector.setStyle(style);
        }, 6000 / speedInput.value);
        console.log('tokennn::::', token);
        console.log('tokennn::::', token);
      }
    };

    const stopAnimation = (ended: any) => {
      // alert('stopAnimation');

      animating = false;
      startButton.textContent = 'Start Animation';
      clearInterval(token);
      timer = -1;
      if (!ended) {
        vector.setStyle(style);
      }
    };

    startButton.addEventListener('click', startAnimation, false);

    if (this.lastSimulationDesc == 'on') {
      await this.datacrowdService
        .getLastSimualtionID(this.usercode)
        .then((res) => {
          if (res != 'null') {
            this.LastSimualtionID = res;
            console.log('getLastSimualtionID>>>.', this.LastSimualtionID);
            if (this.LastSimualtionID != null) {
              if (!localStorage.getItem('locSimulId')) {
                localStorage.setItem('locSimulId', this.LastSimualtionID[0]);
                localStorage.setItem('userCode', this.LastSimualtionID[1]);
              }
            }
          }
        });
    } else {
      //localStorage.setItem("locSimulId",'');
    }
    $('#cotravelerpopup').css('display', 'none');
    $('#btspopup').css('display', 'none');

    this.map.addLayer(this.vectorLayer4);

    this.map.on('click', (evt) => {
      this.getNearest(evt.coordinate).then((coordStreet) => {
        console.log('coordStreet:::::::::', coordStreet);
        const lastPoint = this.points1[this.points1.length - 1];
        const pointsLength = this.points1.push(coordStreet);
        this.createFeature(coordStreet);

        if (pointsLength < 2) {
          // alert('Click to add another point');
          // this.msgEl.innerHTML = 'Click to add another point';
          return;
        }

        const point1 = lastPoint.join();
        const point2 = coordStreet.join();

        fetch(this.urlOsrmRoute + point1 + ';' + point2)
          .then((response) => {
            return response.json();
          })
          .then((json) => {
            if (json.code !== 'Ok') {
              // this.msgEl.innerHTML = 'No route found.';
              return;
            }
   

            // this.msgEl.innerHTML = 'Route added';
            this.createRoute(json.routes[0].geometry);
          });
      });
    });

     

    if ((window.parent as any) && (window.parent as any).AV2_userCode) {
      this.usercode = (window.parent as any).AV2_userCode;
    }
    this.value = false;
    $('#popupGrid').css('display', 'none');
    // $('#tabledatabtn').css('display', 'none');
 
    }

  ngOnChanges(changes: any) {
    console.log("changes",changes);
    console.log("currentValue",changes.changes.currentValue);
  
    if(typeof changes.changes.currentValue=="undefined"){
  
    }else{
      if(changes.changes.currentValue.action=="addnewSenario"){
  let senarioParentName=changes.changes.currentValue.simulID;
  
  this.addnewSenario();
      }else{
  
      }
    }
    
  }
 
  getNearest(coord: any): Promise<any> {
    const coord4326 = this.to4326(coord);
    return new Promise((resolve, reject) => {
      fetch(this.urlOsrmNearest + coord4326.join())
        .then((response) => {
          return response.json();
        })
        .then((json) => {
          if (json.code === 'Ok') {
            resolve(json.waypoints[0].location);
          } else {
            reject();
          }
        });
    });
  }

  createFeature(coord: any): void {
    const feature = new ol.Feature({
      type: 'place',
      geometry: new Point(fromLonLat(coord)),
    });
    feature.setStyle(this.styles.icon);
    this.vectorSource4.addFeature(feature);
    this.map.addLayer(new VectorLayer({ source: this.vectorSource4 }));
  }

  createRoute(polyline: any): void {
    const route = new Polyline({
      factor: 1e5,
    }).readGeometry(polyline, {
      dataProjection: 'EPSG:4326',
      featureProjection: 'EPSG:4326',
    });
    const feature = new ol.Feature({
      type: 'route',
      geometry: route,
    });
    feature.setStyle(this.styles.route);
    this.vectorSource4.addFeature(feature);
    this.map.addLayer(new VectorLayer({ source: this.vectorSource4 }));
  }

  to4326(coord: any): any {
    return transform(
      [parseFloat(coord[0]), parseFloat(coord[1])],
      'EPSG:4326',
      'EPSG:4326'
    );
  }


  async ngAfterViewInit() {
 
    await this.httpClient
      .get('../../../../../assets/assetsOffline/output.geojson')
      .subscribe((data: any) => {
        console.log('222222222222222222222');
        this.geoJsonData = data;
        const format = new GeoJSON();
        this.features = this.geoJsonData.features;
        console.log('geoJsonData=', this.geoJsonData);
        console.log('features=', this.features);

        const vectorSource = new VectorSource({
          features: this.features.map(
            (feature: any) => new ol.Feature(feature)
          ),
        });

        this.vectorLayer = new VectorLayer({
          source: vectorSource,
        });
      });
 

    // this.map.setTarget(this.mapContainer.nativeElement);
    // const format = new GeoJSON();

    
    
    await this.httpClient
    .get('../assets/custom.geo.json')
    .subscribe((res: any) => {
      console.log('3333333333333');
     
      console.log('regionnnn=111111111111', res);
this.regiondata=res;

    });

   let url='../assets/custom.geo.json';
   console.log('url=111111111111', url);
     this.vectorSourceCountry = new VectorSource({
    
      format: new GeoJSON(),
    });
    console.log('vectorSourceCountry :', this.vectorSourceCountry);
 
    fetch(url)
    .then(function(response) {
        return response.json();
    })
    .then( (json)=> {
      
       
        var features = new  GeoJSON().readFeatures(json);
        this.vectorSourceCountry .addFeatures(features);
        console.log('getFeatures :', this.vectorSourceCountry.getFeatures() );

    })
    .catch(function(error) {
        console.error('Error:', error);
    });

 
   console.log("vectorSource-------",this.vectorSourceCountry.getFeatures());
 
  const layer=  new VectorLayer({
      source: this.vectorSourceCountry,   
    });
   
  //this.map.addLayer(layer);
  }



  private deactivateDrawInteraction() {
    if (this.drawInteraction) {
      this.map.removeInteraction(this.drawInteraction);
      this.drawInteraction = null;
    }
  }

 
  drawCircle() {
    this.countries=[];
    this.deactivateDrawInteraction();
    this.drawInteraction = new Draw({
      source: this.vectorSource,
      type: 'Circle',
      freehand: true,
    });
    
    this.drawInteraction.setActive(true);

    console.log('drawType:', this.drawInteraction);
     let id:any;
    this.drawInteraction.on('drawend', async (event: any) => {

      this.centerCoordinates = event.feature.getGeometry().getCenter();
      this.radius = event.feature.getGeometry().getRadius() * 90000;
      console.log('radius in meters:', this.radius);

      const sourceProjection = this.map.getView().getProjection();
      const targetProjection = 'EPSG:900913';
      this.radiusInMeters = transformExtent(
        [0, 0, this.radius, this.radius],
        sourceProjection,
        targetProjection
      )[2];

      this.lon = this.centerCoordinates[0];
      this.lat = this.centerCoordinates[1];

      console.log('radius in meters:', this.radiusInMeters);
      if (this.radiusInMeters > this.circleLimit) {
        Swal.fire({
          text: 'Circle radius is too big',
          backdrop: false,
        });
        this.deactivateDrawInteraction();
        this.drawInteraction.setActive(false);
        this.drawInteraction = null;
      } else {
        var polygon_extent = event.feature.getGeometry()!.getExtent();
        // console.log("polygon_extent-----------",polygon_extent);
        console.log("polygon_extent.-------------",polygon_extent);
        console.log("this.vectorSourceCountry.-------------",this.vectorSourceCountry);
  
        this.vectorSourceCountry.forEachFeatureIntersectingExtent(polygon_extent,  (feature:any) => {
          console.info(feature);
          console.info('isoo---------',feature.values_.iso_a2);
          let iso=feature.values_.iso_a2;
          this.countries.push(feature.values_.iso_a2);
          this.countries= [...new Set(this.countries)];
        });
          console.log("this.countries.-------------",this.countries);

          
      let  C_countryCodes:any;

     await  this.datacrowdService.getcountry(this.countries).then((ress:any)=>{
        console.log('getcountry>>>>',ress);
        // C_subregion=ress[0];
        // C_region=ress[1];
        // C_Country=ress[2];
        C_countryCodes=ress;
      
      }) 
        if (event.feature) {
          this.coordinatesArray.push({
            Bounds: '',
            ID: event.feature.ol_uid,
            Name: '',
            PolyBoundsCoords: '',
            Type: event.target.type_,
            Value: '',
            center: { lng: this.lon, lat: this.lat },
            ol_uid: event.feature.ol_uid,
            radius: this.radiusInMeters,
            selectedStartDate: '',
            selectedEndDate: '',
            leafletid: event.feature.ol_uid,
            countrycodes:this.convertCountryCode(C_countryCodes)
          });

          id=event.feature.ol_uid

        console.log('coordinatesArray>>>>',this.coordinatesArray);

        }
 
        this.deactivateDrawInteraction();

        this.featureID = this.featureID + 1;
        event.feature.setProperties({
          id: this.featureID,
        });
      }

      this.displayCircleForLoop(this.lon,this.lat,this.radiusInMeters,id);

    });

    
    this.drawShapes.push(this.drawInteraction);
  
    this.map.addInteraction(this.drawInteraction);
   
 
 
   
  }

  selectedFeature() {
    if (select !== null) {
      this.selectedFeatures.forEach((feature: any) => {});
    }
  }

  drawPoint() {
    this.countries=[];
    this.deactivateDrawInteraction();
    this.drawInteraction = new Draw({
      source: this.vectorSource,
      type: 'Point',
    });

    this.drawInteraction.on('drawend', async (event: any) => {
      this.coordinates = event.feature.getGeometry().getCoordinates();
      const [lng, lat] = this.coordinates;
      var polygon_extent = event.feature.getGeometry()!.getExtent();
      // console.log("polygon_extent-----------",polygon_extent);

      this.vectorSourceCountry.forEachFeatureIntersectingExtent(polygon_extent,  (feature:any) => {
        console.info(feature);
        console.info('isoo---------',feature.values_.iso_a2);
        let iso=feature.values_.iso_a2;
        this.countries.push(feature.values_.iso_a2);
        this.countries= [...new Set(this.countries)];
      });
        console.log("this.countries.-------------",this.countries);

        let  C_countryCodes:any;

     await   this.datacrowdService.getcountry(this.countries).then((ress:any)=>{
         console.log('getcountry>>>>',ress);
         // C_subregion=ress[0];
         // C_region=ress[1];
         // C_Country=ress[2];
         C_countryCodes=ress;
       
       }) 
      this.coordinatesArray.push({
        Bounds: '',
        ID: event.feature.ol_uid,
        Name: '',
        PolyBoundsCoords: '',
        Type: event.target.type_,
        Value: '',
        center: { lng: lng, lat: lat },
        ol_uid: event.feature.ol_uid,
        radius: '',
        selectedStartDate: '',
        selectedEndDate: '',
        leafletid: event.feature.ol_uid,
        countrycodes:this.convertCountryCode(C_countryCodes)
      });
      this.deactivateDrawInteraction();
    });

    this.map.addInteraction(this.drawInteraction);
  }

  drawLineString() {
    this.countries=[];
    this.deactivateDrawInteraction();
    this.drawInteraction = new Draw({
      source: this.vectorSource,
      type: 'LineString',
    });
    let points:any[] = [];
    this.drawInteraction.on('drawend', async (event: any) => {
      this.coordinates = event.feature.getGeometry().getCoordinates();
      this.length =
        event.feature
          .getGeometry()
          .getLength({ projection: this.map.getView().getProjection() }) *
        100000;
      
      if (this.length < parseInt(this.polygoneLimit)) {
        for (let i = 0; i < this.coordinates.length; i++) {
          const vertex = this.coordinates[i];
          const [lng, lat] = vertex;
          points.push({ lng, lat });
        }
        var polygon_extent = event.feature.getGeometry()!.getExtent();
        // console.log("polygon_extent-----------",polygon_extent);
  
        this.vectorSourceCountry.forEachFeatureIntersectingExtent(polygon_extent,  (feature:any) => {
          console.info(feature);
          console.info('isoo---------',feature.values_.iso_a2);
          let iso=feature.values_.iso_a2;
          this.countries.push(feature.values_.iso_a2);
          this.countries= [...new Set(this.countries)];
        });
          console.log("this.countries.-------------",this.countries);
          let  C_countryCodes:any;

       await   this.datacrowdService.getcountry(this.countries).then((ress:any)=>{
           console.log('getcountry>>>>',ress);
           // C_subregion=ress[0];
           // C_region=ress[1];
           // C_Country=ress[2];
           C_countryCodes=ress;
         
         }) 
        const centroid = this.calculateCentroid(points);
        this.coordinatesArray.push({
          Bounds: points,
          ID: event.feature.ol_uid,
          Name: '',
          PolyBoundsCoords: '',
          Type: 'Polyline',
          Value: '',
          center: centroid,
          ol_uid: event.feature.ol_uid,
          radius: '',
          selectedStartDate: '',
          selectedEndDate: '',
          leafletid: event.feature.ol_uid,
          countrycodes:this.convertCountryCode(C_countryCodes)
        });

        this.deactivateDrawInteraction();
        this.displayPolylineForLoop(points);

      } else {
        Swal.fire({
          text: 'polyline limit exceeded',
          backdrop: false,
        });
        this.deactivateDrawInteraction();
        this.drawInteraction.setActive(false);
        this.drawInteraction = null;
      }
    });
    this.map.addInteraction(this.drawInteraction);
  }
  drawPolygon() {
    this.countries=[];

    this.deactivateDrawInteraction();
    this.drawInteraction = new Draw({
      source: this.vectorSource,
      type: 'Polygon',
    });
    this.drawInteraction.setActive(true);
    let points:any[] = [];
    this.drawInteraction.on('drawend', async (event: any) => {
      this.coordinates = event.feature.getGeometry().getCoordinates()[0];
      const geometry = event.feature.getGeometry();
      const polygon = new Polygon(geometry.getCoordinates());

      const area = getArea(polygon) * 10000;

      let output;
      output = Math.round(area * 100000000) / 100;
      const areaInSquareMeter = output;
      
      // let region=this.getRegionForCoordinates(this.coordinates);
      // console.log("region--------------",region)
      

      if (output > parseInt(this.polygoneLimit)) {
        Swal.fire({
          text: 'polygon limit exceeded',
          backdrop: false,
        });
        this.deactivateDrawInteraction();
        this.drawInteraction.setActive(false);
        this.drawInteraction = null;
      } else {
        for (let i = 0; i < this.coordinates.length; i++) {
          const vertex = this.coordinates[i];
          const [lng, lat] = vertex;
          points.push({ lng, lat });
        }
        const centroid = this.calculateCentroid(points);
        var polygon_extent = event.feature.getGeometry()!.getExtent();
        // console.log("polygon_extent-----------",polygon_extent);
        console.log("polygon_extent.-------------",polygon_extent);
  
        this.vectorSourceCountry.forEachFeatureIntersectingExtent(polygon_extent,  (feature:any) => {
          console.info(feature);
          console.info('isoo---------',feature.values_.iso_a2);
          let iso=feature.values_.iso_a2;
          this.countries.push(feature.values_.iso_a2);
          this.countries= [...new Set(this.countries)];
        });

          console.log("this.countries.-------------",this.countries);

          let  C_countryCodes:any;

         await this.datacrowdService.getcountry(this.countries).then((ress:any)=>{
           console.log('getcountry>>>>',ress);
           // C_subregion=ress[0];
           // C_region=ress[1];
           // C_Country=ress[2];
           C_countryCodes=ress;
         
         });

        this.coordinatesArray.push({
          Bounds: points,
          ID: event.feature.ol_uid,
          Name: '',
          PolyBoundsCoords: points,
          Type: event.target.type_,
          Value: '',
          center: centroid, // Set the center as 0,0 for polygons
          ol_uid: event.feature.ol_uid,
          radius: '',
          selectedStartDate: '',
          selectedEndDate: '',
          leafletid: event.feature.ol_uid,
          countrycodes:this.convertCountryCode(C_countryCodes)
        });
        console.log('coordinatesArray>>>>',this.coordinatesArray)
        this.deactivateDrawInteraction();
        this.displayPolygonForLoop(points);

      }
    });
    this.map.addInteraction(this.drawInteraction);
  }
  calculateCentroid(points: Array<{ lng: number; lat: number }>) {
    let x = 0;
    let y = 0;

    for (let i = 0; i < points.length; i++) {
      const { lng, lat } = points[i];
      x += lng;
      y += lat;
    }

    const totalPoints = points.length;
    const centroidLng = x / totalPoints;
    const centroidLat = y / totalPoints;

    return { lng: centroidLng, lat: centroidLat };
  }

  defaultCursor() {
    this.deactivateDrawInteraction();
  }

  clearShapes() {
    this.vectorSource.clear(); // Clear the vector source to remove all drawings
    this.darwLayer.getSource()?.clear();
    this.map.removeLayer(this.clusters2);
    this.map.removeLayer(this.clusters);
    this.magnifiedMap.removeLayer(this.clustersLoop);
    this.map.removeLayer(this.clustersdev);
    this.map.removeLayer(this.fixedMarkersGroup);
    this.map.removeLayer(this.circleLayer);
    this.magnifiedMap.removeLayer(this.circleLayerLoop);
    this.map.removeLayer(this.layerPolygon);
    this.magnifiedMap.removeLayer(this.layerPolygonLoop);
    this.map.removeLayer(this.layerPolyline);
    this.magnifiedMap.removeLayer(this.layerPolylineLoop);
    this.map.removeLayer(this.vectorLayerSector);
    $('#popup2').css('display', 'none');
    $('#popupGrid').css('display', 'none');
    $('#showMeasure2').css('display', 'none');
    $('#btspopup').css('display', 'none');
    $('#cotravelerpopup').css('display', 'none');
    $('.timeline').css('display', 'none');
    const tooltipElements = this.el.nativeElement.querySelectorAll(
      'div[id="measureTooltip"]'
    );
    tooltipElements.forEach((element: HTMLElement) => {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    });

    const tooltipElements1 =
      this.el.nativeElement.querySelectorAll('div[id="tooltip"]');
    tooltipElements1.forEach((element: HTMLElement) => {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    });

    const tooltipElements2 = this.el.nativeElement.querySelectorAll(
      'div[id="nameTooltip"]'
    );
    tooltipElements2.forEach((element: HTMLElement) => {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    });
    const tooltipElements3 = this.el.nativeElement.querySelectorAll(
      'div[id="dateTooltip"]'
    );
    tooltipElements3.forEach((element: HTMLElement) => {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    });
    const tooltipElements4 = this.el.nativeElement.querySelectorAll(
      'div[class="ol-tooltip ol-tooltip-static"]'
    );
    tooltipElements4.forEach((element: HTMLElement) => {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    });

    this.map.removeLayer(this.shapeMeasure);

    for (const layer of this.fixedMarkersGroupsArray) {
      this.map.removeLayer(layer);
    }
    for (const layer of this.shapesArray) {
      console.log('shapesArray>>>', this.shapesArray);
      this.map.removeLayer(layer);
    }
    for (const layer of this.polylines) {
      this.map.removeLayer(layer);
    }
    for (const layer of this.polylinesLoop) {
      this.magnifiedMap.removeLayer(layer);
    }

    for (const layer of this.circles) {
      this.map.removeLayer(layer);
    }

    for (const layer of this.polygons) {
      this.map.removeLayer(layer);
    }

    for (const layer of this.polygonsLoop) {
      this.magnifiedMap.removeLayer(layer);
    }


    for (const layer of this.heats) {
      this.map.removeLayer(layer);
    }

    this.circles.forEach((layer) => {
      this.map.removeLayer(layer);
    });
    this.circlesLoop.forEach((layer) => {
      this.magnifiedMap.removeLayer(layer);
    });

   
    this.clusters2Array.forEach((cluster) => {
      this.map.removeLayer(cluster);
    });
   
    this.clusterFeaturesLoop.forEach((marker) => {
      this.map.removeLayer(marker);
    });

    this.fixedMarkersGroupsArray.forEach((marker) => {
      this.map.removeLayer(marker);
    });

    this.fixedbtsGroupArray.forEach((bts: any) => {
      this.map.removeLayer(bts);
    });
     
    this.manysimularray.forEach((cluster: any) => {
      this.map.removeLayer(cluster);
    });
    for (const layer of this.clusterFeaturesdev) {
      this.map.removeLayer(layer);
    }
    for (const layer of this.Fixedsectorarray) {
      this.map.removeLayer(layer);
    }

    for (const layer of this.circles) {
       this.map.removeLayer(layer);
    }


   
    this.datacrowdService.deleteLastSimualtionID(this.usercode);
    this.Fixedsectorarray=[];
    this.features = [];
    this.clusterFeatures = [];
    this.circlesLoop=[];
    this.circles=[];
    this.clusterFeaturesLoop = [];
    this.clusters = [];
    this.clusters2 = [];
    this.manysimularray=[];
    this.manysimularrayLoop=[];
    this.Devices = '';
    this.circleFeature = null;
    this.circleFeatureLoop = null;
    this.ObjectID = [];
    this.coordinatesArray = [];
    this.shapesArray = [];
    this.FixedElementsPopup = false;
    this.clusterFeatures2 = [];
    this.display = false;
    this.uniqueNames = [];
    this.heats = [];
    this.coordinatesArray = [];
    this.clusterFeatures2 = [];
    this.clusters2Array = [];
    localStorage.clear();
    this.fixedbtsGroupArray = [];
    this.fixedMarkersGroupsArray = [];
    this.indexTimeline = 0;
    this.fixedBtsArray = [];
    this.fixedbtsGroup = [];
    this.clusterFeaturesdev = [];
    this.sourcedev = [];
    this.clustersourcedev = [];
    this.removesectors=[];
    this.Datatable1=[];
    this.Datatable=[];
    this.Datatablereverse=[];
    this.polygons=[];
    this.polygonsLoop=[];
    this.index=0;
    this.displaysectors=false;
    this.displayclusters=false;
    $('.timeline').css('display', 'none');
    this.openTable = false;
    $('#tabletest').css('display', 'none');
    this.isRunning = false;
    this.map.removeLayer(this.fixedbtsGroup);
    for (const layer of this.featuresSector) {
      console.log('sectorarray>>>', this.featuresSector);
      this.map.removeLayer(layer);
    }
    this.closeTCDPopup();
    this.Fixedsectorarray = [];
    this.CdrData = [];
    this.aggridmarker = false;
    $('#animatonbar').css('display', 'none');
    this.fixedbtsGroup = [];
    localStorage.clear();
    this.fixedBtsArray = [];
    this.clusterSourcebts = [];
    this.sourcebts = [];
    this.featuresSector=[];
    this.vectorLayerSector=[];
    this.showbarstart=false;
    this.showbarreverse=false;
    this.speedTime=1;
    this.Datatable=[];
    this.openTable=false;
    this.devaddgrparray=[];
    this.hitsaddgrpnb=[];
    this.routedatajson=[];
    this.routeDevicestable=[];
    this.routeDevices=[];
    this.datajson=[];
    this.clearRoutes();
    this.routeDevicestable=[];

    this.A_locSimulId = '';
    if (this.isSimul == true) {
      console.log('clearforsimul>>>>');
      (window.parent.parent.parent[7] as any).clearforsimul();
    } else {
      console.log('clearshapes>>>>');

      (window.parent.parent.parent[7] as any).clearshapes();
    }
  }

  // async getSimulationData(obj: any) {
  //   try {
  //     let response = await this.datacrowdService.getSimulationData(obj);
  //     console.log(' response getSimulationData>>>>>> ', response);
  //     return response;
  //   } catch (error) {
  //     console.log(' failure exception ', error);
  //   }
  // }
  changeInteraction = () => {
    if (select !== null) {
      this.map.removeInteraction(select);
    }
    if (select !== null) {
      this.map.addInteraction(select);
      select.getFeatures().on('add', (event) => {
        var properties = event.element.getProperties();
        this.selectedFeatureID = properties['id'];
      });
      select.on('select', (e) => {
        let selected = e.selected; // Selected features
        let deselected = e.deselected; // Deselected features

        selected.forEach((feature) => {
          this.selectedFeatures.push(feature);
        });

        deselected.forEach((feature) => {
          remove(this.selectedFeatures.getArray(), feature);
        });
      });
    }
  };
  binddata(
    obj0: any,
    obj1: any,
    obj2: any,
    obj3: any,
    obj4: any,
    obj5: any,
    hits: any
  ) {
    var hit = 0;
    if (hits == '') {
      hit = 1;
    } else {
      hit = hits;
    }
    if (this.isSelectMode) {
      return;
      new Feature(new Point([parseFloat(obj0), parseFloat(obj1)]));
    } else {
      return new Feature(new Point([parseFloat(obj0), parseFloat(obj1)]));
    }
  }

  dateTtoDate(date: any) {
    date = new Date(date);
    date = String(date);
    var getDate = date.split('GMT');
    date = getDate[0];
    return date;
  }

  deleteFeature() {
    this.deleteMode = true;
    this.select = new Select({
      condition: (event) => {
        return event.originalEvent.button === 2; // Right-click condition
      },
    });
    this.map.addInteraction(this.select);
    this.select.getFeatures().on('add', (feature: any) => {
      console.log('feature in delete fct::::', feature);
      if (feature.element.getGeometry() instanceof Point) {
        this.deleteMode = false;
        this.map.removeInteraction(this.select);
        $('#datepicker').css('display', 'none');
        console.log("It's a Point!");
      } else {
        this.deleteMode = true;
        // alert("in select")
        // feature.preventDefault();
        // $('#datepicker').css('display', 'none');

        // console.log("feature111111111<>>>>>>>>><<<><<><><>:",feature.element);
        // console.log("feature::::",feature.element.ol_uid);
        this.selectspeificshape = feature.element.ol_uid;
        $('#datepicker').css('display', 'none');

        if (this.deleteMode === true) {
          // alert("tryue")
          Swal.fire({
            text: 'Do you want to delete this feature?',
            showCancelButton: true,
            backdrop: false,
            confirmButtonText: 'Ok',
          }).then((result) => {
            if (result.isConfirmed) {
              this.deleteMode = false;
              console.log(
                'drawlayerrrrrrrrrrrrrrrrrr::::',
                this.darwLayer.getSource()
              );
              this.darwLayer.getSource()?.removeFeature(feature.element);
              feature.target.remove(feature.element);
              $('#datepicker').css('display', 'none');

              this.shapesArray.forEach((shapeMeasure: any) => {
                shapeMeasure.getSource()?.removeFeature(feature.element);
                feature.target.remove(feature.element);
                console.log(
                  'this.overlaymeasure.pushh:::::',
                  this.overlaymeasure
                );

                const tooltipElements = this.el.nativeElement.querySelectorAll(
                  `div[id="measure_${this.selectspeificshape}"]`
                );
                tooltipElements.forEach((element: HTMLElement) => {
                  if (element.parentNode) {
                    element.parentNode.removeChild(element);
                  }
                });
              });
              this.polylines.forEach((polyline: any) => {
                console.log('polyline in delete fct::;', polyline);
                polyline.getSource()?.removeFeature(feature.element);
                feature.target.remove(feature.element);
                // this.coordinatesArray.filter((a) => a.ID === polyline)
              });
              this.savedelete();
            } else {
              this.deleteMode = false;
              this.map.removeInteraction(this.select);
            }
          });
        } else {
          // alert("nooooooooooooooo")
        }
        $('#datepicker').css('display', 'none');
      }
    });
    $('#datepicker').css('display', 'none');

    this.deleteMode = false;
  }

  savedelete() {
    this.deleteMode = false;
    console.log(
      'this.coordinatesArray1111111111111111111111111111',
      this.coordinatesArray
    );
    console.log('this.selectspeificshape)', this.selectspeificshape);

    this.coordinatesArray = this.coordinatesArray.filter(
      (item) => item.ID !== this.selectspeificshape
    );
    console.log('this.coordinatesArray', this.coordinatesArray);
    this.map.removeInteraction(this.select);
  }

  canceldelete() {
    this.deleteMode = false;
    this.map.removeInteraction(this.select);
    console.log('idddd111111111111111:', this.selectspeificshape);
    console.log('this.coordinatesArray.1111111111:', this.coordinatesArray);
    console.log('this.coordinatesArray.1111111111:', this.coordinatesArray);
    // var type=this.coordinatesArray.find((a) => a.ID === this.selectspeificshape)!['Type'];
    var drawbElement = this.coordinatesArray.find(
      (a) => a.ID === this.selectspeificshape
    );
    var drawbElementId = drawbElement!['ID'];
    console.log('drawbElementId::::', drawbElementId);
    var drawbElementType = drawbElement!['Type'];
    var drawbElementName = drawbElement!['Name'];
    var drawbElementcenter = drawbElement!['center'];
    var drawbElementradius = drawbElement!['radius'];
    var drawbElementPolyBoundsCoords: any;
    drawbElementPolyBoundsCoords = drawbElement!['PolyBoundsCoords'];
    var drawbElementBounds: any;
    drawbElementBounds = drawbElement!['Bounds'];
    if (drawbElementType === 'Circle') {
      var radius = this.coordinatesArray.find(
        (a) => a.ID === this.selectspeificshape
      )!['radius'];
      console.log('radiussssssssssssssssssssssss:', radius);
      const view = this.map.getView();
      const { lng, lat } = drawbElementcenter;
      console.log('centerrrrrrrrrrrrrrrrrrrrrrrrrrrrrr', [lng, lat]);
      const resolution = view.getResolution();
      console.log('resolution', resolution);
      if (resolution === undefined) {
        console.error('Resolution is undefined');
        return;
      }
      const radiusInPixels = drawbElementradius;
      const element3 = lng;
      const element4 = lat;
      this.circleFeature = new ol.Feature(
        new circle(
          transform([element3, element4], 'EPSG:4326', 'EPSG:3857'),
          radiusInPixels * 1.24
        ).transform('EPSG:3857', 'EPSG:4326')
      );
      console.log('radiusssssssssss', drawbElementradius);
      console.log(
        'radiusInPixelsssssssssssssssssssssssssssssssssssssssss',
        radiusInPixels * 1.24
      );
      const circleStyle = new Style({
        fill: new Fill({
          color: '#7eb2fb40',
        }),
        stroke: new Stroke({
          color: '#007FFF',
          width: 3,
        }),
      });
      this.circleFeature.setStyle(circleStyle);
      this.circleLayer = new VectorLayer({
        source: new VectorSource({
          features: [this.circleFeature],
        }),
        zIndex: 0,
      });
      this.circleLayer.ol_uid = drawbElementId;
      // this.circleFeature['ol_uid']=drawbElementId;
      this.displaymodified.push(this.circleFeature);
      this.map.addLayer(this.circleLayer);
      // this.displayshapes.push(this.circleLayer);
    } else if (drawbElementType === 'Polygon') {
      // alert("else")
      let polygonCoords;
      polygonCoords = drawbElementPolyBoundsCoords.map((coord: any) => {
        const element3 = parseFloat(coord.lat);
        const element4 = parseFloat(coord.lng);
        // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
        return [element4, element3];
      });

      console.log('polygonCoords', polygonCoords);

      console.log('coordobj>>>>>>>>>', drawbElementPolyBoundsCoords);

      const geojsonObject = {
        type: 'FeatureCollection',
        crs: {
          type: 'name',
          properties: {
            name: 'EPSG:3857',
          },
        },
        features: [
          {
            type: 'Feature',
            geometry: {
              type: 'Polygon',
              coordinates: [polygonCoords],
            },
          },
        ],
      };
      console.log('geojsonObject>>>>>>>>>', geojsonObject);

      const styles = [
        new Style({
          fill: new Fill({
            color: '#7eb2fb40',
          }),
          stroke: new Stroke({
            color: '#007FFF',
            width: 3,
          }),
          image: new CircleStyle({
            radius: 7,
            fill: new Fill({
              color: '#7eb2fb',
            }),
          }),
        }),
        new Style({
          geometry: (feature: any) => {
            const coordinates = feature.getGeometry().getCoordinates()[0];
            return new MultiPoint(coordinates);
          },
        }),
      ];
      this.sourcePolygon = new VectorSource({
        features: new GeoJSON().readFeatures(geojsonObject),
      });
      console.log('source::::::::::', this.sourcePolygon);
      this.layerPolygon = new VectorLayer({
        source: this.sourcePolygon,
        style: styles,
      });
      this.polygons.push(this.layerPolygon);
      this.map.addLayer(this.layerPolygon);
      // this.displayshapes.push(this.circleLayer);
    } else if (drawbElementType === 'Polyline') {
      const polylineCoords = drawbElementBounds.map((coord: any) => {
        const element3 = parseFloat(coord.lat);
        const element4 = parseFloat(coord.lng);
        // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
        return [element4, element3];
      });
      const styles = [
        new Style({
          fill: new Fill({
            color: '#7eb2fb40',
          }),
          stroke: new Stroke({
            color: '#007FFF',
            width: 3,
          }),
          image: new CircleStyle({
            radius: 7,
            fill: new Fill({
              color: '#7eb2fb',
            }),
          }),
        }),
      ];
      this.sourcePolyline = new VectorSource({
        features: [new Feature(new LineString(polylineCoords))],
      });
      this.layerPolyline = new VectorLayer({
        source: this.sourcePolyline,
        style: styles,
      });
      this.polylines.push(this.layerPolyline);
      this.map.addLayer(this.layerPolyline);
      // this.displayshapes.push(this.circleLayer);
    }
  }

  async refresh() {
    if (!localStorage.getItem('multiselection')) {
      this.multiselection = [];
    } else {
      console.log(
        'multiselection>>>>',
        JSON.parse(localStorage.getItem('multiselection')!)
      );
      console.log(
        'multiselection>>>>',
        JSON.parse(localStorage.getItem('multiselection')!).join()
      );

      this.multiselection = JSON.parse(
        localStorage.getItem('multiselection')!
      ).join();
      if (this.Devices == '' || typeof this.Devices == 'undefined') {
        this.Devices = this.multiselection;
      } else {
        this.Devices = this.Devices + ',' + this.multiselection;
      }
    }
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_ImsiID
    ) {
      this.ImsiID = (window.parent.parent.parent[7] as any).A_ImsiID;
      console.log('imsiiiiiiiiiiiiii:::::', this.ImsiID);
    }

    if (typeof this.ImsiID == 'undefined') {
      this.ImsiID = '';
    }

    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_COUNTRY_CODE
    ) {
      this.countrycode = (window.parent.parent.parent[7] as any).A_COUNTRY_CODE;
      console.log('countrycode:::::', this.countrycode);

      if (typeof this.countrycode !== 'undefined') {
        console.log('countrycode not empty>>>>');

          let numArr = this.countrycode.split(',').map(Number);
      numArr = this.getDistinctNumbers(numArr);
      console.log('numArr>>>>>>>', numArr);
      this.countrycode=numArr;
       await this.datacrowdService.getcountry2(this.countrycode).then((res:any)=>{
          console.log('getcountry2>>>>',res);
          this.countrycode=res;
          this.countrycode=this.convertCountryCode(this.countrycode);
        })
      }

    }else{
      await this.datacrowdService.getALLcountryIDS().then(async (res:any)=>{
        console.log('getALLcountryIDS>>>>',res);

        this.countrycode=res;

         await this.datacrowdService.getcountry2(this.countrycode).then((res:any)=>{
            console.log('getcountry2>>>>',res);
            this.countrycode=res;
            this.countrycode=this.convertCountryCode(this.countrycode);
          })

      })
    }

   
    if (typeof this.countrycode == 'undefined') {
      this.countrycode = '';
    }

    console.log('this.senarioFlag>>',this.senarioFlag)
    console.log('this.senariocount>>>',this.senariocount);

    let A_isSenarioFromcase:any = (window.parent.parent.parent[7] as any).A_isSenarioFromcase;
    console.log("isSimul>>>>>>>>>>>>>>>>>> A_isSenarioFromcase",A_isSenarioFromcase);

    if(A_isSenarioFromcase==true){
   

      await this.datacrowdService.getinternalcode(this.senarioParentName).then((res:any)=>{
        console.log("res> getinternalcode>>>>>>>>>>",res);
        this.internalcode=res[0];
        this.senariocount=res[1];
  
      });
      this.senariocount++;

    }else{

      console.log('this.senarioFlag>>',this.senarioFlag)
      console.log('this.senariocount>>>',this.senariocount);

      if(this.senariocount==0){
        this.senarioParentName=this.simulationid;
        this.firstsenario=this.simulationid;
        this.internalcode=this.simulationid;
  
      }
    }

    let queryjson:any;
    if(this.senarioFlag==true){
       queryjson = {
        reportName: "No Name",
        reportType: this.reportType,
        reportTypeId: this.reportTypeId,
        TimeZone: this.TimeZone,
        RecipientUser: this.RecipientUser,
        DateTimeFrom: this.DateTimeFrom,
        RecipientEmail: this.RecipientEmail,
        DateTimeTo: this.DateTimeTo,
        Coordinates:this.coordinatesArray,
        meter: this.meter,
        Devices: this.Devices,
        isCSVAttached: this.isCSVAttached,
        dataType: this.dataType,
        telephoneNumber: this.telephoneNumber,
        // provider: "",
        EDGEHEIGHT: this.EDGEHEIGHT,
        simulationId: this.simulationid,
        userCode: this.usercode,
        imsiId: this.ImsiID,
        countryCode: this.countrycode,
        senario:{parent:this.senarioParentName,
          internalcode:this.internalcode,
         senariocount: this.senariocount.toString()},
         BtsTypeSlected:""

      };
      this.routeDevices=this.Devices;
      this.dataservice1.setroutedevices(this.Devices);
      (window.parent.parent.parent[7] as any).A_IsSenario=true;

// if(this.senariocount==0 && this.addnewsenariocount==0){
//   (window.parent.parent.parent[7] as any).A_isfirstSenario=true;
// }else{
//   (window.parent.parent.parent[7] as any).A_isfirstSenario=false;

// }
    
console.log("senariocount", this.senariocount);
console.log("addnewsenariocount", this.addnewsenariocount);

console.log("zzzz", (window.parent.parent.parent[7] as any));
this.routeDevices=this.Devices;
    }else{
       queryjson = {
        reportName: "No Name",
        reportType: this.reportType,
        reportTypeId: this.reportTypeId,
        TimeZone: this.TimeZone,
        RecipientUser: this.RecipientUser,
        DateTimeFrom: this.DateTimeFrom,
        RecipientEmail: this.RecipientEmail,
        DateTimeTo: this.DateTimeTo,
        Coordinates: this.coordinatesArray,
        meter: this.meter,
        Devices: this.Devices,
        isCSVAttached: this.isCSVAttached,
        dataType: this.dataType,
        telephoneNumber: this.telephoneNumber,
        // provider: "",
        EDGEHEIGHT: this.EDGEHEIGHT,
        simulationId: this.simulationid,
        userCode: this.usercode,
        imsiId: this.ImsiID,
        countryCode: this.countrycode,
        senario:"-1",
        BtsTypeSlected:""
      };
      this.routeDevices=this.Devices;
      this.dataservice1.setroutedevices(this.Devices);
    }

    // let queryjson = {
    //   reportName: 'No Name',
    //   reportType: this.reportType,
    //   reportTypeId: this.reportTypeId,
    //   TimeZone: this.TimeZone,
    //   RecipientUser: this.RecipientUser,
    //   DateTimeFrom: this.DateTimeFrom,
    //   RecipientEmail: this.RecipientEmail,
    //   DateTimeTo: this.DateTimeTo,
    //   Coordinates: this.coordinatesArray,
    //   meter: this.meter,
    //   Devices: this.Devices,
    //   isCSVAttached: this.isCSVAttached,
    //   dataType: this.dataType,
    //   telephoneNumber: this.telephoneNumber,
    //   // provider: "",
    //   EDGEHEIGHT: this.EDGEHEIGHT,
    //   simulationId: this.simulationid,
    //   userCode: this.usercode,
    //   imsiId: this.ImsiID,
    //   countryCode: this.countrycode,
    // };
    console.log('queryjson>>>>', queryjson);
    if (this.reportType == 1) {
      if (this.coordinatesArray.length == 0) {
        Swal.fire({
          text: 'Select at least one Shape',
          backdrop: false,
        });
      } else {
        this.test(queryjson);
      }
    }  else if (this.reportType == 3) {
      console.log("zzz>>>", this.coordinatesArray.length)






      if (!this.coordinatesArray || this.coordinatesArray.length < 2) {

      
        Swal.fire({
          text: 'Select more than one Area',
          backdrop: false,
          // showCancelButton: true,
        });

        localStorage.clear();
      } else {
        let shouldExecuteTest = true; // Flag variable

        await this.coordinatesArray.forEach(obj => {
          console.log('obj>>>>>>>>', obj);
          if (
            obj.selectedStartDate === '' ||
            obj.selectedEndDate === '' ||
            typeof obj.selectedStartDate === 'undefined' ||
            typeof obj.selectedEndDate === 'undefined'
          ) {
          
            Swal.fire({
              text: 'Date each Shape!!!',
              backdrop: false,
            });
            shouldExecuteTest = false; // Set the flag to false if any object meets the condition
          }
        });

        if (shouldExecuteTest) {
          this.test(queryjson);
        }

      }

    }else if (this.reportType == 2) {
      if (
        (this.Devices == '' || typeof this.Devices == 'undefined') &&
        (this.multiselection == '' ||
          typeof this.multiselection == 'undefined') &&
        (this.telephoneNumber == '' ||
          typeof this.telephoneNumber == 'undefined')
      ) {
        Swal.fire({
          text: 'No filter exists!',
          backdrop: false,
          // showCancelButton: true,
        });
      } else {
        this.test(queryjson);
      }
    } else if (this.reportType == 7) {
      if (
        (this.Devices == '' || typeof this.Devices == 'undefined') &&
        (this.multiselection == '' ||
          typeof this.multiselection == 'undefined') &&
        (this.telephoneNumber == '' ||
          typeof this.telephoneNumber == 'undefined')
      ) {
        Swal.fire({
          text: 'No Devices exists!',
          backdrop: false,
          // showCancelButton: true,
        });
      } else {
        var words = this.Devices.split(',');
        if (words.length < 2) {
          Swal.fire({
            text: 'devices should be at least 2',
            backdrop: false,
            // showCancelButton: true,
          });
        } else {
          this.test(queryjson);
        }
      }
    } else if (
      (this.reportType == 9 || this.reportType == 8) &&
      this.coordinatesArray.length != 0
    ) {
      $('#showMeasure2').css('display', '');
      this.displayclusters=true;
      this.displaysectors=true;
      this.map.removeLayer(this.fixedbtsGroup);
      this.fixedbtsGroup = [];
 
      this.map.removeLayer(this.vectorLayerSector);
      this.vectorLayerSector = [];

console.log("fixedbtsGroup-----",this.fixedbtsGroup);
console.log("vectorLayerSector-----",this.vectorLayerSector);

      let numArr2: any = [];
      //remove the old fixedelements and markers
      // this.map.removeLayer(this.clusters);
      this.map.removeLayer(this.fixedMarkersGroup);
      // this.features=[];
      // this.clusterFeatures = [];
      this.fixedMarkersGroup = [];

      Swal.fire({
        text: 'Please Choose Fixed Element Type',
        showCancelButton: true,
        backdrop: false,
        confirmButtonText: 'BTS',
        cancelButtonText: 'Fixed Element',
      }).then((result) => {
        if (result.isConfirmed) {
          this.BtsTypeSlected='BTS';
          queryjson.BtsTypeSlected=this.BtsTypeSlected;
 
          this.datacrowdService
            .ScanfixedElements(queryjson)
            .then((response) => {
              response = response;
              console.log('ScanfixedElements>>>', response);

              this.datacrowdService
                .getVcisfixedelementsID(this.simulationid)
                .then(async (response2: any) => {
                  response2 = response2;
                  if (response2.length === 0) {
                    Swal.fire({
                      text: 'No fixed Elements Available',
                      backdrop: false,
                      // showCancelButton: true,
                    });
                  }

                  console.log('getVcisfixedelementsID>>>', response2);
                  console.log('in fixed element scan');

                  for (var i = 0; i < response2.length; i++) {
                    console.log(response2[i][0]);
                    numArr2.push(Number(response2[i][0]));
                  }
                  console.log('numArr>>', numArr2);

                  if (
                    (window.parent as any) &&
                    (window.parent as any).AV2_userCode
                  ) {
                    this.usercode = (window.parent as any).AV2_userCode;
                  }

                  this.displaybtsrefresh(numArr2);

                  if (this.reportType == 9) {
                    console.log('in fixed element activity scan');
                    await this.datacrowdService
                      .getsimulation2(queryjson)
                      .then((res) => {
                        this.datajson = res;
                        if (this.datajson.markerPositions.length === 0) {
                          Swal.fire({
                            text: 'No Data Available',
                            backdrop: false,
                            // showCancelButton: true,
                          });
                          (window.parent.parent.parent[7] as any).clearshapes();
                        } else {
                          console.log(
                            'getsimultion response >>>>',
                            this.datajson
                          );

                          const tooltipElements3 =
                            this.el.nativeElement.querySelectorAll(
                              'div[id="dateTooltip"]'
                            );
                          tooltipElements3.forEach((element: HTMLElement) => {
                            if (element.parentNode) {
                              this.renderer.setStyle(
                                element,
                                'display',
                                'none'
                              );
                            }
                          });
                          // this.addheats(this.datajson);
                          for (
                            let i = 0;
                            i < this.datajson.markerPositions.length;
                            i++
                          ) {
                            let point = new Point([
                              parseFloat(this.datajson.markerPositions[i][1]),
                              parseFloat(this.datajson.markerPositions[i][0]),
                            ]);
                            this.marker = new Feature({
                              geometry: point,
                              name: 'Marker',
                            });
                            this.marker.set('type', 'marker');
                            this.marker.set(
                              'info',
                              this.datajson.markerPositions[i]
                            );
                            this.clusterFeatures.push(this.marker);
                          }

                          this.source = new VectorSource({
                            features: this.clusterFeatures,
                          });
                          this.clusterSource = new Cluster({
                            source: this.source,
                          });

                          const styleCache: { [key: number]: Style } = {};

                          this.clusters = new VectorLayer({
                            source: this.clusterSource,
                            style: (feature) => {
                              const size = feature.get('features').length;
                              let style = styleCache[size];
                              if (!style) {
                                if (size < 20) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#2EFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#2EFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 20 && size < 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FFFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#FFFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FF660040',
                                      }),
                                      fill: new Fill({
                                        color: '#FF660090',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                }
                                styleCache[size] = style;
                              }
                              return style;
                            },
                          });

                          this.map.addLayer(this.clusters);
                          if (this.reportType == 2) {
                            this.clustersZoom(this.datajson);
                          }
                          this.clustersZoom(this.datajson);
                        }
                      });
                  }

                  this.map.addInteraction(select);
                  numArr2 = [];
                });


       



                
            });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          this.BtsTypeSlected='Fixed Element';

          this.datacrowdService
            .ScanfixedElements(queryjson)
            .then((response) => {
              response = response;
              console.log('ScanfixedElements>>>', response);

              this.datacrowdService
                .getVcisfixedelementsID(this.simulationid)
                .then(async (response2: any) => {
                  response2 = response2;
                  if (response2.length === 0) {
                    Swal.fire({
                      text: 'No fixed Elements Available',
                      backdrop: false,
                      // showCancelButton: true,
                    });
                  }

                  console.log('getVcisfixedelementsID>>>', response2);
                  console.log('in fixed element scan');

                  for (var i = 0; i < response2.length; i++) {
                    numArr2.push(Number(response2[i][0]));
                  }
                  console.log('numArr>>', numArr2);

                  if (
                    (window.parent as any) &&
                    (window.parent as any).AV2_userCode
                  ) {
                    this.usercode = (window.parent as any).AV2_userCode;
                  }
                  console.log('response------', response);

                  this.datacrowdService
                    .getfixedelementsObject(numArr2)
                    .then((res) => {
                      console.log('getfixedelementsObject>>>>', res);
                      localStorage.setItem(
                        'fixedelementsObject',
                        JSON.stringify(res)
                      );
                      this.displayFixedElements(res);
                    });

                  if (this.reportType == 9) {
                    console.log('in fixed element activity scan');
                    await this.datacrowdService
                      .getsimulation2(queryjson)
                      .then((res) => {
                        this.datajson = res;
                        if (this.datajson.markerPositions.length === 0) {
                          Swal.fire({
                            text: 'No Data Available',
                            backdrop: false,
                            // showCancelButton: true,
                          });
                          (window.parent.parent.parent[7] as any).clearshapes();
                        } else {
                          console.log(
                            'getsimultion response >>>>',
                            this.datajson
                          );

                          const tooltipElements3 =
                            this.el.nativeElement.querySelectorAll(
                              'div[id="dateTooltip"]'
                            );
                          tooltipElements3.forEach((element: HTMLElement) => {
                            if (element.parentNode) {
                              this.renderer.setStyle(
                                element,
                                'display',
                                'none'
                              );
                            }
                          });
                          // this.addheats(this.datajson);
                          for (
                            let i = 0;
                            i < this.datajson.markerPositions.length;
                            i++
                          ) {
                            let point = new Point([
                              parseFloat(this.datajson.markerPositions[i][1]),
                              parseFloat(this.datajson.markerPositions[i][0]),
                            ]);
                            this.marker = new Feature({
                              geometry: point,
                              name: 'Marker',
                            });
                            this.marker.set('type', 'marker');
                            this.marker.set(
                              'info',
                              this.datajson.markerPositions[i]
                            );
                            this.clusterFeatures.push(this.marker);
                          }

                          this.source = new VectorSource({
                            features: this.clusterFeatures,
                          });
                          this.clusterSource = new Cluster({
                            source: this.source,
                          });

                          const styleCache: { [key: number]: Style } = {};

                          this.clusters = new VectorLayer({
                            source: this.clusterSource,
                            style: (feature) => {
                              const size = feature.get('features').length;
                              let style = styleCache[size];
                              if (!style) {
                                if (size < 20) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#2EFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#2EFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 20 && size < 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FFFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#FFFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FF660040',
                                      }),
                                      fill: new Fill({
                                        color: '#FF660090',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                }
                                styleCache[size] = style;
                              }
                              return style;
                            },
                          });

                          this.map.addLayer(this.clusters);
                          if (this.reportType == 2) {
                            this.clustersZoom(this.datajson);
                          }
                          this.clustersZoom(this.datajson);
                        }
                      });
                  }

                  this.map.addInteraction(select);
                  numArr2 = [];
                });
            });
        }
        console.log("queryjson>>>>>>",queryjson);



      });


      let obj22:any={
        senarioParentName:this.senarioParentName,
        simulationid:this.simulationid,
        Action:"addnewMenu",
        senariocount:this.senariocount,
        senarioFlag:this.senarioFlag,
        reportType:this.reportType
      }
        
      // this.senarioIdOutput.emit(obj22);
          this.navbarSimulId=obj22;


    } else {
      this.test(queryjson);
    }


        // this.test(queryjson)
        if(this.senarioFlag==true){
          this.senariocount++;
    
        }
    this.Devices = [];
  }

  direction() {
    if (this.reportType == 6) {
      console.log('multiselection>>>', this.multiselection);

      this.datacrowdService
        .getdirection(
          this.simulationid,
          JSON.parse(localStorage.getItem('multiselection')!)
        )
        .then((res) => {
          console.log('data>><<', res);

          let data: any = res;
          for (var i = 0; i < data.length; i++) {
            this.displayPolyline(data[i], 'direction');
          }
        });
    } else {
      console.log('report type', this.reportType);
      console.log('report type222', window.parent.parent.parent[7] as any);

      Swal.fire({
        text: 'The type must be Device History Pattern!',
        backdrop: false,
        // showCancelButton: true,
      });
    }
  }

  getdirectionByTime() {
    if (this.reportType == 6) {
      console.log('multiselection>>>', this.multiselection);

      this.datacrowdService
        .getdirectionByTime(
          this.simulationid,
          JSON.parse(localStorage.getItem('multiselection')!)
        )
        .then((res) => {
          console.log('data>><<', res);

          let data: any = res;
          console.log('dataaaaaaaaaaaaaaaaaaaaa:', data);
          for (var i = 0; i < data.length; i++) {
            console.log('inside  for');
            console.log('iiiiiii inside for::::::', data[i]);

            this.displayPolyline(data[i], 'directionByTime');
          }
        });
    } else {
      console.log('report type', this.reportType);
      console.log('report type222', window.parent.parent.parent[7] as any);

      Swal.fire({
        text: 'The type must be Device History Pattern!',
        backdrop: false,
        // showCancelButton: true,
      });
    }
  }

  tcd() {
    this.displaysectors=true;
    $('.timeline').css('display', 'flex');

    this.isTcd = true;
    let src: any;

    console.log('datajson>>>', this.datajson);

    this.CdrData = this.datajson;

    console.log('x', this.CdrData);
    console.log('x000', this.CdrData.BTS[0]);

    this.CdrRowData = [];

    for (let i = 0; i < this.CdrData.BTS[0].length; i++) {
      //this.displayFixedElements(this.CdrData[0][i]);

      this.displayBTS(this.CdrData.BTS[0][i].BTS, this.CdrData.BTS[0][i].INFO);
      console.log('this.CdrData[0][i]>>>', this.CdrData.BTS[0][i].BTS);

      for (let j = 0; j < this.CdrData.BTS[0][i].SECTORS.length; j++) {
        this.drawarc(
          Number(this.CdrData.BTS[0][i].BTS.LATITUDE),
          Number(this.CdrData.BTS[0][i].BTS.LONGITUDE),
          this.SectorMeter,
          Number(this.CdrData.BTS[0][i].SECTORS[j]),
          Number(this.CdrData.BTS[0][i].SECTORS[j]),
          this.SectorColor,
          this.CdrData.BTS[0][i].SINFO,
          ''
        );
      }
    }
    this.sourcebts = new VectorSource({
      features: this.fixedBtsArray,
    });
    console.log('source>>>', this.source);

    this.clusterSourcebts = new Cluster({
      minDistance: 100,
      source: this.sourcebts,
    });
    console.log('clusterSource>>>>>>>>', this.clusterSource);
    const styleCache: { [key: number]: Style } = {};

    this.fixedbtsGroup = new VectorLayer({
      source: this.clusterSourcebts,
      zIndex: 9999,
      style: function (bts) {
        // console.log("cluster:::::",bts);
        const size = bts.get('features').length;
        // console.log('size::::',size);
        let style = styleCache[size];
        if (!style) {
          src = '/assets/assetsOffline/icons/BTS.png';

          styleCache[size] = style;
        }

        return new Style({
          image: new Icon({
            src: src,
            scale: 0.8,
            // size:[32,32]
          }),
          text: new Text({
            text: size.toString(),
            fill: new Fill({ color: '#fff' }),
          }),
        });
      },
    });

    this.fixedbtsGroup.set('A_Type', 'tcd');
    this.fixedbtsGroupArray.push(this.fixedbtsGroup);
    this.map.addLayer(this.fixedbtsGroup);
    this.displayBtsOnMap();

    this.displayarc();

    this.isTcd = false;
  }

  displaySectorGrid(properties: any) {
    let AzimuthSelected = '';
    let lngSelcted = '';
    let latSelected = '';
    let TechnologySelected = '';
    let SINFO: any[];
    $('#TCDContent').css('display', '');
    console.log('propertiesTCDContent >>>>>>>>>>>>>>>>', properties);
    console.log('Azimuth:', properties['Azimuth']);
    AzimuthSelected = properties['Azimuth'];
    console.log('Lng:', properties['lng']);
    lngSelcted = properties['lng'];
    console.log('Lat:', properties['lat']);
    latSelected = properties['lat'];
    console.log('Technology:', properties['Technology']);
    TechnologySelected = properties['Technology'];
    console.log('sihnfo--------------', properties['SINFO']);
    SINFO = properties['SINFO'];

    let findedSectors: any = SINFO.filter((element: any) => {
      return (
        element[7] == AzimuthSelected &&
        element[4] === latSelected &&
        element[5] === lngSelcted
      );
    });
    console.log('findedSectors when right click', findedSectors);
    this.TcdRowData = [];
    findedSectors.forEach((element: any, key: any) => {
      var jsonaggrid = {
        imsi_id: element[0],
        imei_id: element[1],
        service_provider_id: element[2],
        usage_timeframe: this.dateTtoDate(element[3]),
        location_latitude: element[4],
        location_longitude: element[5],
        cgi_id: element[6],
        location_azimuth: element[7],
        type_id: element[8],
        phone_number: element[9],
        Technology: element[10],
      };

      this.TcdRowData.push(jsonaggrid);
    });

    console.log('TcdRowData', this.TcdRowData);
    //// open popup
    var popup: any = this.elem_id('TCDContent');
    var popup_closer = this.elem_id('popup-closer')!;
    var popup_content = this.elem_id('popup-content');

    this.olpopupOverlay = new ol.Overlay({
      element: popup,
      autoPan: false,
    });

    this.olpopupOverlay.setPosition([lngSelcted, latSelected]);
    this.map.addOverlay(this.olpopupOverlay);
  }

  async test(obj: any) {
    // alert('refresh')rf
    
    let resp: any;
    this.deletingModeEnabled = true; //this variable is used for disable the popup of naming aoi (naming shapes)

    if (this.clusters) {
      this.map.removeLayer(this.clusters);
      this.clusters = [];
      this.clusterFeatures = [];
    }
    if (this.clustersdev) {
      this.map.removeLayer(this.clustersdev);
      this.clustersdev = [];
      this.clusterFeaturesdev = [];
    }

    if(this.reportType==6){
      let arrayofCountryCodes:any[]=[];
      console.log("Coord>>>",this.coordinatesArray);
    console.log("arrayofCountryCodes first >>>",arrayofCountryCodes);

      if(this.coordinatesArray){
        this.coordinatesArray.forEach((element:any,key:any)=>{
    console.log("arrayofCountryCodes before >>>",arrayofCountryCodes);

      console.log("element>>>",element.countrycodes);
      console.log("element.countrycodes[0]>>>",element.countrycodes[0]);
      if(element.countrycodes[0].length>0){
      arrayofCountryCodes.push(element.countrycodes[0]);
    }
    console.log("arrayofCountryCodes after >>>",arrayofCountryCodes);

        })


      }
      console.log("queryjson countryCode",obj.countryCode);
      obj.countryCode.forEach((element2:any,key:any)=>{
        arrayofCountryCodes.push(element2);

      });
      console.log("arrayofCountryCodes final>>>",arrayofCountryCodes);
      console.log("arrayofCountryCodes2222>>>",this.convertCountryCode2(arrayofCountryCodes));
      obj.countryCode=this.convertCountryCode2(arrayofCountryCodes);
    }




    console.log('obj queryjson>>', obj);
    this.datajson = await this.getSimulationData(obj);
    console.log('datajson>>>>', this.datajson);

    if (this.reportType == '10') {
      console.log('coordinates------', obj.Coordinates);
      this.scandevices();
    } else if (this.reportType == '11') {
      await this.datacrowdService
        .getsimualtion(this.simulationid, this.usercode)
        .then((res) => {
          this.datajson = res;
          console.log('getsimultion response tcddddd>>>>', this.datajson);
        });
      this.tcd();
    } else if (this.reportType == '1') {
      console.log('7777777777777777777777777777');
      if (this.clustersdev) {
        this.map.removeLayer(this.clustersdev);
        this.clustersdev = [];
        console.log('88888888888888888888888888');
      }
      this.displayClusters(this.datajson);
      this.clustersZoom(this.datajson);
    } else {
      $('.timeline').css('display', 'none');

      if (
        this.datajson.markerPositions == null ||
        this.datajson.heatCoords == null ||
        typeof this.datajson == 'undefined' ||
        typeof this.datajson.heatCoords == 'undefined'
      ) {
        Swal.fire({
          text: 'No Data Available',
          backdrop: false,
        });
        (window.parent.parent.parent[7] as any).clearshapes();
      } else {
        this.map.removeLayer(this.clusters);
        this.clusterFeatures = [];
        this.clusters = [];

        if (this.reportType == '7') {
          for (var i = 0; i < this.datajson.Shape.length; i++) {
            console.log('lngg:::', this.datajson.Shape[i].Center.lng);
            console.log('lat:::', this.datajson.Shape[i].Center.lat);
            console.log('Radius:::', this.datajson.Shape[i].Radius);
            console.log('Id:::', this.datajson.Shape[i].Id);

            this.displayCircle(
              this.datajson.Shape[i].Center.lng,
              this.datajson.Shape[i].Center.lat,
              this.datajson.Shape[i].Radius,
              this.datajson.Shape[i].Id
            );
          }
        }

        const tooltipElements3 = this.el.nativeElement.querySelectorAll(
          'div[id="dateTooltip"]'
        );
        tooltipElements3.forEach((element: HTMLElement) => {
          if (element.parentNode) {
            this.renderer.setStyle(element, 'display', 'none');
          }
        });

        // this.addheats(this.datajson);
        this.displayClusters(this.datajson);
        this.clustersZoom(this.datajson);
      }
    }
    $('#showMeasure2').css('display', '');
 
 
    console.log("zzsenariocount",this.senariocount)
    console.log("zzaddnewsenariocount",this.addnewsenariocount)
    console.log("zzsenarioFlag",this.senarioFlag);
  
  if(this.senarioFlag==true && this.senariocount==1 && this.addnewsenariocount==0){
    Swal.fire({
      text: "Please save The Simulation",
      icon: 'warning',
      // showCancelButton: true,
    });
 
    if (!localStorage.getItem('multiselection')) {
      this.multiselection = [];
    }
 localStorage.clear();
  }


  let obj22:any={
    senarioParentName:this.senarioParentName,
    simulationid:this.simulationid,
    Action:"addnewMenu",
    senariocount:this.senariocount,
    senarioFlag:this.senarioFlag,
    reportType:this.reportType
  }
    
  // this.senarioIdOutput.emit(obj22);
      this.navbarSimulId=obj22;
}

  displayClusters(datajs: any) {
    console.log("11111111111111---------",datajs);
    datajs={
      markerPositions:datajs
    };

    console.log("2222222222222222222---------",datajs);

    this.displayclusters=true;
    for (let i = 0; i < datajs.markerPositions.length; i++) {
      let point = new Point([
        parseFloat(datajs.markerPositions[i][3]),
        parseFloat(datajs.markerPositions[i][4]),
      ]);
      this.marker = new Feature({
        geometry: point,
        name: 'Marker',
      });
      this.marker.set('type', 'marker');
      this.marker.set('info', datajs.markerPositions[i]);
      this.clusterFeatures.push(this.marker);
    }
    this.clusterFeaturesLoop=this.clusterFeatures;
    
    this.source = new VectorSource({
      features: this.clusterFeatures,
    });
    

    this.clusterSource = new Cluster({
      source: this.source,
    });
    this.sourceLoop = new VectorSource({
      features: this.clusterFeatures,
    });
    this.clusterSourceLoop = new Cluster({
      source: this.sourceLoop,
    });
    const styleCache: { [key: number]: Style } = {};

    this.clusters = new VectorLayer({
      source: this.clusterSource,
      style: (feature) => {
        const size = feature.get('features').length;
        let style = styleCache[size];
        if (!style) {
          if (size < 20) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#2EFF2E40',
                }),
                fill: new Fill({
                  color: '#2EFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 20 && size < 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FFFF2E40',
                }),
                fill: new Fill({
                  color: '#FFFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FF660040',
                }),
                fill: new Fill({
                  color: '#FF660090',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
          }
          styleCache[size] = style;
        }
        return style;
      },
    });

    this.clustersLoop = new VectorLayer({
      source: this.clusterSourceLoop,
      style: (feature) => {
        const size = feature.get('features').length;
        let style = styleCache[size];
        if (!style) {
          if (size < 20) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#2EFF2E40',
                }),
                fill: new Fill({
                  color: '#2EFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 20 && size < 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FFFF2E40',
                }),
                fill: new Fill({
                  color: '#FFFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FF660040',
                }),
                fill: new Fill({
                  color: '#FF660090',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
          }
          styleCache[size] = style;
        }
        return style;
      },
    });


    this.clusters.set('type', 'marker');
    this.manysimularray.push(this.clusters);
    this.manysimularrayLoop.push(this.clustersLoop);

    this.map.addLayer(this.clusters);
    this.magnifiedMap.addLayer(this.clustersLoop);

    if (this.reportType == 2) {
      this.clustersZoom(datajs);
    }


    this.map.on('singleclick', (event) => {
      // get the feature you clicked
      const feature = this.map.forEachFeatureAtPixel(
        event.pixel,
        (feature: any) => {
          return feature;
        }
      );
    });
  }



  async refreshSimulation() {

    $('#popup2').css('display', 'none');
    this.value=false;
    console.log('data from jsp=', window.parent.parent.parent[7] as any);

    console.log('ang window.parent>>>', window.parent);
    console.log('ang isMapAngular  >>', (window.parent as any).isMapAngular);
    console.log(
      'ang isMapAngular window.parent.parent >>',
      window.parent.parent as any
    );
    console.log('ang isMapAngular  >>', window.parent.parent.parent as any);
    console.log(
      'ang isMapAngular  7 >>',
      window.parent.parent.parent[7] as any
    );

    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_DateTimeFrom
    ) {
      console.log(
        'A_DateTimeFrom',
        (window.parent.parent.parent[7] as any).A_DateTimeFrom
      );
    } else {
      console.log('not A_DateTimeFrom');
    }
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_IsSimul
    ) {
      console.log(
        'A_IsSimul',
        (window.parent.parent.parent[7] as any).A_IsSimul
      );
      this.isSimul = (window.parent.parent.parent[7] as any).A_IsSimul;
      console.log('isSimul>>', this.isSimul);
    }

    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_simulationid
    ) {
      this.simulationid = (
        window.parent.parent.parent[7] as any
      ).A_simulationid;
      // alert("simulation id:");
    }

    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_usercode
    ) {
      this.usercode = (window.parent.parent.parent[7] as any).A_usercode;
    }
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_isFixedElements
    ) {
      console.log(
        'A_isFixedElements',
        (window.parent.parent.parent[7] as any).A_isFixedElements
      );
      this.isFixedElements = (
        window.parent.parent.parent[7] as any
      ).A_isFixedElements;
      console.log('isFixedElements>>', this.isFixedElements);
    }
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_isFixedElementsall
    ) {
      console.log(
        'A_isFixedElementsall',
        (window.parent.parent.parent[7] as any).A_isFixedElementsall
      );
      this.isFixedElementsall = (
        window.parent.parent.parent[7] as any
      ).A_isFixedElementsall;
      console.log('isFixedElementsall>>', this.isFixedElementsall);
    }
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_isAOi
    ) {
      this.isAOi = (window.parent.parent.parent[7] as any).A_isAOi;
    }

    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_ImsiID
    ) {
      this.ImsiID = (window.parent.parent.parent[7] as any).A_ImsiID;
    }

    if (
      typeof (window.parent.parent.parent[7] as any).A_ImsiID == 'undefined'
    ) {
      this.ImsiID = '';
    }

    console.log('simulationid', this.simulationid);

    if (this.isSimul == true) {

      
      this.reportType = (window.parent.parent.parent[7] as any).A_reportType;
      this.reportTypeId = (window.parent.parent.parent[7] as any).A_reportTypeId;
      let A_isSenarioFromcase:any = (window.parent.parent.parent[7] as any).A_isSenarioFromcase;
      console.log("isSimul>>>>>>>>>>>>>>>>>> A_isSenarioFromcase",A_isSenarioFromcase);
      console.log("isSimul>>>>>>>>>>>>>>>>>> ",(window.parent.parent.parent[7] as any));

      if(A_isSenarioFromcase==true){
        if(this.senarioFlag==true){

        }else{
          $("#toggleColor").click();

        }
        this.senarioParentName=this.simulationid;
        let obj:any={
          senarioParentName:this.senarioParentName,
          simulationid:this.simulationid,
          Action:"DisplayFromSenario",
          senariocount:this.senariocount,
          senarioFlag:this.senarioFlag,
          reportType:this.reportType


        }
        // this.senarioIdOutput.emit(this.senarioParentName);
        this.navbarSimulId=obj;

      }else{
        if(this.senarioFlag==false){

        }else{
          $("#toggleColor").click();

        }
      }
    
      console.log('isSimul>>>>>>>>>>>>>>>>>>');
      (window.parent.parent.parent[7] as any).ResetBooleanVar();
      this.clearShapes();
      this.isAOi = false;
      this.isFixedElements = false;
      this.isSimul = false;

      if (!localStorage.getItem('userCode')) {
        if (!localStorage.getItem('locSimulId')) {
          localStorage.setItem('locSimulId', this.simulationid);
          localStorage.setItem('userCode', this.usercode);
          console.log('usercodee::::::::', this.usercode);
        }
      }

      this.datacrowdService
        .getsimualtion(this.simulationid, this.usercode)
        .then((res: any) => {
          this.datajson = res;
          console.log('getsimultion response >>>>', this.datajson);
          const tooltipElements3 = this.el.nativeElement.querySelectorAll(
            'div[id="dateTooltip"]'
          );
          tooltipElements3.forEach((element: HTMLElement) => {
            if (element.parentNode) {
              this.renderer.setStyle(element, 'display', 'none');
            }
          });
          if(this.datajson.CDR){
          $('#showMeasure2').css('display', '');

            this.tcd();
          }else{
            this.displayClusters(this.datajson);

          }
          // this.addheats(this.datajson);

          this.clustersZoom(this.datajson);
          $('#showMeasure2').css('display', '');
        });

      await this.datacrowdService
        .getExecutionParam(this.simulationid)
        .then((res) => {
          this.ExecutionParam = res;
          console.log(
            'getExecutionParam response >>>>',
            this.ExecutionParam.Coordinates
          );
          let shapecoord = this.ExecutionParam.Coordinates;

          localStorage.setItem('coordsimul', JSON.stringify(shapecoord));
          var shapeId = 0;

          for (var j = 0; j < shapecoord.length; j++) {
            if (shapecoord[j].Type == 'Circle') {
              this.displayCircle(
                shapecoord[j].center.lng,
                shapecoord[j].center.lat,
                shapecoord[j].radius,
                shapecoord[j].leafletid
              );
            } else if (shapecoord[j].Type == 'Polygon') {
              this.displayPolygon(shapecoord[j]);
            } else if (shapecoord[j].Type == 'Polygon') {
              this.displayPolyline(shapecoord[j], 'simulation');
            }
          }
        });
    } else if (this.isFixedElements == true) {
      console.log('isFixedElements>>>>>>>>>>>>>>>>>>');

      (window.parent.parent.parent[7] as any).ResetBooleanVar();
      console.log(
        'A_ObjectID=',
        (window.parent.parent.parent[7] as any).A_ObjectID
      );

      //alert("isFixedElements");
      //this.ObjectID=(window.parent.parent.parent[7] as any).A_ObjectID;

      //this.ObjectID=this.ObjectID.push((window.parent.parent.parent[7] as any).A_ObjectID);
      if (this.ObjectID) {
        this.ObjectID.push((window.parent.parent.parent[7] as any).A_ObjectID);
      } else {
        this.ObjectID = [(window.parent.parent.parent[7] as any).A_ObjectID];
      }
      console.log('ObjectID>>>>>', this.ObjectID);
      console.log('ObjectID join>>>>>', this.ObjectID.join());
      console.log(
        'ObjectID join2>>>>>',
        this.ObjectID.join().split(',').map(Number)
      );
      // let arr = this.ObjectID.split(",");
      // console.log("arr>>>>>",arr);
      // let numArr = arr.map(Number);
      // console.log("numArr>>>>>",numArr);
      let numArr = this.ObjectID.join().split(',').map(Number);
      numArr = this.getDistinctNumbers(numArr);
      console.log('numArr>>>>>>>', numArr);

      if (this.isFixedElementsall == true) {
        this.datacrowdService.getfixedelementsObject2(numArr).then((res) => {
          console.log('getfixedelementsObject2>>>>', res);
          localStorage.setItem('getfixedelementsObject2', JSON.stringify(res));
          this.displayFixedElements(res);
        });
      } else {
        this.datacrowdService.getfixedelementsObject(numArr).then((res) => {
          console.log('getfixedelementsObject>>>>', res);
          localStorage.setItem('fixedelementsObject', JSON.stringify(res));
          this.displayFixedElements(res);
        });
      }

      this.isAOi = false;
      this.isSimul = false;

      this.isFixedElements = false;
      this.isFixedElementsall = false;
    } else if (this.isAOi == true) {
      var shapeId = 0;
      console.log('isAOi>>>>>>>>>>>>>>>>>>');

      (window.parent.parent.parent[7] as any).ResetBooleanVar();
      this.AoiIds = (window.parent.parent.parent[7] as any).A_AoiIds;
      console.log('AoiIds>>>>>', this.AoiIds);
      for (let i = 0; i < this.AoiIds.length; i++) {
        this.datacrowdService.getSelectedShape(this.AoiIds[i]).then((res) => {
          console.log('getSelectedShape>>>>', res);
          this.Aoiresp = res;
          console.log('myres=', this.Aoiresp);
          console.log('myresName=', this.Aoiresp.Name);
          if (this.Aoiresp.Type == 'Circle') {
            this.displayCircle(
              this.Aoiresp.center.lng,
              this.Aoiresp.center.lat,
              this.Aoiresp.radius,
              shapeId
            );
            this.map.getView().setZoom(12);
            this.map
              .getView()
              .setCenter([this.Aoiresp.center.lng, this.Aoiresp.center.lat]);
            this.createshapeNameTooltip(
              this.Aoiresp.Name,
              [this.Aoiresp.center.lng, this.Aoiresp.center.lat],
              this.AoiIds,
              'nameTooltip'
            );
          } else if (this.Aoiresp.Type == 'Rectangle') {
          } else if (this.Aoiresp.Type == 'Polygon') {
            this.displayPolygon(this.Aoiresp);
            this.map.getView().setZoom(12);
            this.map
              .getView()
              .setCenter([this.Aoiresp.center.lng, this.Aoiresp.center.lat]);
            this.createshapeNameTooltip(
              this.Aoiresp.Name,
              [this.Aoiresp.center.lng, this.Aoiresp.center.lat],
              this.AoiIds,
              'nameTooltip'
            );
          } else if (this.Aoiresp.Type == 'Polyline') {
            this.displayPolyline(this.Aoiresp, 'simulation');
            this.map.getView().setZoom(12);
            this.map
              .getView()
              .setCenter([this.Aoiresp.center.lng, this.Aoiresp.center.lat]);
            this.createshapeNameTooltip(
              this.Aoiresp.Name,
              [this.Aoiresp.center.lng, this.Aoiresp.center.lat],
              this.AoiIds,
              'nameTooltip'
            );
          }
   
        });
      }

      //console.log('objAoi>>>>',this.objAoi)
      this.isSimul = false;
      this.isFixedElements = false;
      this.isAOi = false;
    } else {
      console.log('activityscan>>>>>>>>>>>>>>>>>>');

      if (localStorage.getItem('coordsimul')) {
        let coordsimul: any = localStorage.getItem('coordsimul');
        coordsimul = JSON.parse(coordsimul);
        console.log('coordsimul>>>', coordsimul);
      }

      (window.parent.parent.parent[7] as any).ResetBooleanVar();
      // alert("not simullllll>>>>.")
      this.reportName = (window.parent.parent.parent[7] as any).A_reportName;
      this.reportType = (window.parent.parent.parent[7] as any).A_reportType;
      this.reportTypeId = (
        window.parent.parent.parent[7] as any
      ).A_reportTypeId;
      this.TimeZone = (window.parent.parent.parent[7] as any).A_TimeZone;
      this.RecipientUser = (
        window.parent.parent.parent[7] as any
      ).A_RecipientUser;
      console.log('reportName', this.reportName);
      console.log('reportTypeId', this.reportTypeId);
      console.log('reportType', this.reportType);
      console.log('TimeZone', this.TimeZone);
      console.log('RecipientUser', this.RecipientUser);
      if (this.RecipientUser === 'empty' || this.RecipientUser === null) {
        this.RecipientUser = '';
      }
      //this.RecipientUser="";

      if ((window.parent.parent.parent[7] as any).A_DateTimeFrom != null) {
        this.DateTimeFrom = this.datePipe.transform(
          (window.parent.parent.parent[7] as any).A_DateTimeFrom,
          'MM/dd/yyyy HH:mm'
        );
      }

      if ((window.parent.parent.parent[7] as any).A_DateTimeTo != null) {
        this.DateTimeTo = this.datePipe.transform(
          (window.parent.parent.parent[7] as any).A_DateTimeTo,
          'MM/dd/yyyy HH:mm'
        );
      }

      if (this.RecipientEmail === 'empty' || this.RecipientEmail === null) {
        this.RecipientEmail = '';
      }

      if (this.RecipientUser === 'empty' || this.RecipientUser === null) {
        this.RecipientUser = '';
      }
      //this.RecipientEmail = (window.parent.parent.parent[7] as any).A_RecipientEmail ;
      this.RecipientEmail = '';
      this.Devices = (window.parent.parent.parent[7] as any).A_Devices;
      this.isCSVAttached = (
        window.parent.parent.parent[7] as any
      ).A_isCSVAttached;
      this.dataType = (window.parent.parent.parent[7] as any).A_dataType;
      this.telephoneNumber = (
        window.parent.parent.parent[7] as any
      ).A_telephoneNumber;
      this.usercode = (window.parent.parent.parent[7] as any).A_usercode;
      this.meter = (window.parent.parent.parent[7] as any).A_meter;
      this.EDGEHEIGHT = (window.parent.parent.parent[7] as any).A_EDGEHEIGHT;
      console.log('Devices', this.Devices);
      console.log('isCSVAttached', this.isCSVAttached);
      console.log('dataType', this.dataType);
      console.log('telephoneNumber', this.telephoneNumber);
      console.log('usercode', this.usercode);
      console.log('meter', this.meter);
      console.log('EDGEHEIGHT', this.EDGEHEIGHT);

      //check id the device is empty or null from jsp
      if (this.Devices === 'empty' || this.Devices === 'null') {
        this.Devices = '';
      }

      if (this.dataType === 'empty' || this.dataType === 'null') {
        // this.dataType = "9,10,11,12,8,6,16,17,15";
        this.dataType = '';
      }

      if (this.meter === 'empty' || this.meter === 'null') {
        this.meter = '';
      }
      if (this.EDGEHEIGHT === 'empty' || this.EDGEHEIGHT === 'null') {
        this.EDGEHEIGHT = '';
      }
      if (
        this.telephoneNumber == 'empty' ||
        this.telephoneNumber == 'null' ||
        typeof this.telephoneNumber == 'undefined'
      ) {
        this.telephoneNumber = '';
      }

      if (
        this.isCSVAttached == 'empty' ||
        this.isCSVAttached == 'null' ||
        typeof this.isCSVAttached == 'undefined'
      ) {
        this.isCSVAttached = '';
      }

      if (
        this.TimeZone == 'empty' ||
        this.TimeZone == 'null' ||
        typeof this.TimeZone == 'undefined'
      ) {
        this.TimeZone = '';
      }
      if (this.DateTimeFrom != null) {
        this.refresh();
        // let simulcoord = JSON.parse(localStorage.getItem("coords"));
        // this.Coord = simulcoord;

        // this.map.setView(this.Coord[0].center,20);

        // this.updateMap2(
        //   JSON.parse(localStorage.getItem("ZOOM")),
        //   simulcoord[0].center
        // );

        //  }
      } else {
        localStorage.clear();
      }
    }
  }

  openPopupp(evt: any, olpopup: any) {
    console.log('evt:::::::', evt);
    this.GridPosition = this.map.getCoordinateFromPixel(
      evt.mapBrowserEvent.pixel
    );
    olpopup.setPosition(this.GridPosition);
    this.value = true;
  }

  namingshapes(e: any) {
    console.log('e namingshapes>>>>>', e);
    console.log('Coord>>>>>', this.coordinatesArray);
    // this.shapeName=this.data.getShapename();
    console.log('namingshapes shapeName=', this.shapeName);
    console.log('namingshapes typeof shapeName=', typeof this.shapeName);

    if (
      this.shapeName == '' ||
      this.shapeName === undefined ||
      typeof this.shapeName === 'undefined'
    ) {
    } else {
      this.shapeId = e.ol_uid;
      console.log('shapeId', this.shapeId);
      const checkValue = this.data.getCheckValue();
      if (checkValue) {
        this.saveCheckbox = true;
        console.log('checked:', this._checked);
      } else {
        this.saveCheckbox = false;
        console.log('not checked:', this._checked);
      }

     
      console.log(' this.shapeName1>>>', this.shapeName);
      this.shapeNamepopup = this.shapeName;
      console.log('coordinatesArray>>>', this.coordinatesArray);
      console.log('shapeId', this.shapeId);
      this.coordinatesArray.find((a) => a.ID === e.ol_uid)!['Name'] =
        this.shapeName;
      console.log(
        'HEREEEEEE',
        this.coordinatesArray.find((a) => a.ID === e.ol_uid)
      );
      var drawbElement = this.coordinatesArray.find((a) => a.ID === e.ol_uid);
      console.log('drawbElement>>>', drawbElement);
      var drawbElementId = drawbElement!['ID'];
      console.log('drawbElementId>>>>>>>>>>', drawbElementId);
      var drawbElementType = drawbElement!['Type'];
      console.log('drawbElementType>>>', drawbElementType);

      var drawbElementName = drawbElement!['Name'];
      console.log('drawbElementName>>>', drawbElementName);

      var drawbElementcenter = drawbElement!['center'];
      console.log('drawbElementcenter>>>>>>>>>>>', drawbElementcenter);
      console.log('drawbElement>>>', drawbElement);

      //  this.coordinatesArray.find(a => a.ID === drawbElementId)!['Name'] = this.shapeName;
      console.log(' this.shapeName2>>>', this.shapeName);
      if (this.saveCheckbox == true) {
        console.log('shapeId', e.ol_uid);

        const elementIndex = this.coordinatesArray.findIndex(
          (a) => a.ID === e.ol_uid
        );
        console.log('elementIndex', elementIndex);

        if (elementIndex !== -1) {
          this.coordinatesArray[elementIndex].Name = this.shapeName;
        } else {
          console.log(' this.shapeNamegpttttttttt>>>', this.shapeName);
          // Handle the case where the element is not found
          console.log('Element not found.');
        }

        // this.shapesArray.find((a:any) => a.ID === drawbElementId)['Name'] = this.shapeName;
        var myObject = this.coordinatesArray.find(
          (a) => a.ID === drawbElementId
        );
        var myObjectId = myObject!['ID'];
        var myObjectName = myObject!['Name'];
        var myObjectCenter = myObject!['center'];
        var { lng, lat } = myObjectCenter;

        console.log('myObject>>>>', myObject);
        console.log('myObjectId>>>>', myObjectId);
        console.log('myObjectName>>>>', myObjectName);
        console.log('myObjectCenter>>>>', myObjectCenter);

        this.shapesDataArray.push({
          Name: this.shapeName,
          Type: drawbElementType,
        });

        console.log('shapesDataArray>>>', this.shapesDataArray);
        var drawbElementName = drawbElement!['Name'];
        var shapeValueData = this.coordinatesArray.find(
          (a) => a.ID === drawbElementId
        )!['Value'];
        // USER  id combo

        this.datacrowdService
          .SaveShape(JSON.stringify(myObject))
          .then((res) => {
            console.log('myObjectdatacrowdService>>>>>>>>>>>', myObject);
            console.log('SaveShape>>>>>>>>>>>', res);

            console.log('myObjectName>>>>', myObjectName);
            console.log('myObjectCenter>>>>', myObjectCenter);
            console.log('res.status>>>>', res.status);

            if (res.status == 'fail') {
              Swal.fire({
                text: 'Shape Name Already Exists',
                backdrop: false,
                // showCancelButton: true,
              });
            } else {
              var { lng, lat } = drawbElementcenter;

              console.log('lng:', lng);
              console.log('lat:', lat);
              var position = [lng, lat];
              console.log('position::::::', position);
              console.log('shapeName::::::', myObjectName);
              console.log('e.ol_uid::::::', e.ol_uid);
              this.shapeName = myObjectName;
              this.createshapeNameTooltip(
                this.shapeName,
                position,
                e.ol_uid,
                'nameTooltip'
              );
            }
          });
        console.log('22222222222222222:', this.shapeName);
      } else {
        console.log(
          'this.coordinatesArray.find(a => a.ID)',
          this.coordinatesArray.find((a) => a.ID === e.ol_uid)
        );

        var { lng, lat } = drawbElementcenter;
        console.log('lng:', lng);
        console.log('lat:', lat);
        var position = [lng, lat];

        this.shapeName = this.shapeName;
        this.createshapeNameTooltip(
          this.shapeName,
          position,
          e.ol_uid,
          'nameTooltip'
        );
      }

      this.shapeName = '';
    }
  }

  createshapeNameTooltip(name: any, position: any, id: any, idtooltip: any) {
    let newshapeNameTooltipElement = document.createElement('div');
    newshapeNameTooltipElement.className = 'ol-tooltip ol-tooltip-measure';
    newshapeNameTooltipElement.innerHTML = name;
    newshapeNameTooltipElement.id = idtooltip;
    let newshapeNameTooltip = new Overlay({
      element: newshapeNameTooltipElement,
      offset: [0, -15],
      positioning: 'bottom-center',
      stopEvent: false,
      insertFirst: false,
    });
    newshapeNameTooltip.setPosition(position);
    this.map.addOverlay(newshapeNameTooltip);

    this.nameshapeparameters.push({
      name: name,
      position: position,
      id: id,
    });

    this.overlayshapename.push({
      id: id,
      nameOverlay: name,
      element: newshapeNameTooltip,
    });
  }

  createDateTooltip(name: any, position: any, id: any, idtooltip: any) {
    let newshapeNameTooltipElement = document.createElement('div');
    newshapeNameTooltipElement.className = 'ol-tooltip ol-tooltip-measure';
    newshapeNameTooltipElement.innerHTML = name;
    newshapeNameTooltipElement.id = idtooltip;
    let newshapeNameTooltip = new Overlay({
      element: newshapeNameTooltipElement,
      offset: [0, -15],
      positioning: 'bottom-center',
      stopEvent: false,
      insertFirst: false,
    });
    newshapeNameTooltip.setPosition(position);
    this.map.addOverlay(newshapeNameTooltip);

    this.dateshapeparameters.push({
      name: name,
      position: position,
      id: id,
    });

    this.overlayshapedate.push({
      id: id,
      nameOverlay: name,
      element: newshapeNameTooltip,
    });
  }

  deleteOverlayById(id: any) {
    const indexToDelete = this.overlayshapename.findIndex(
      (overlay) => overlay.id === id
    );
    if (indexToDelete !== -1) {
      const overlayToRemove = this.overlayshapename[indexToDelete];
      this.map.removeOverlay(overlayToRemove.element);
      this.overlayshapename.splice(indexToDelete, 1);
    }
  }

  closepopupnameshape(e: any) {
    console.log('closepopupnameshape:::::', e);

    this.nameshapevalue = false;
    $('#popup11111').hide();
    this.shapeName = this.data.getShapename();
    // this.namingshapes(event);
    //  this.shapeName= this.data.getShapename();
    console.log('Shapename>>>>>: ', this.data.getShapename());
    this.namingshapes(e);
  }
  closepopupnameshapeCancel() {
    this.nameshapevalue = false;
    $('#popup11111').hide();
  }

  search(searchTerm: any) {

    console.log("searchTerm------",searchTerm);
    const feature: any = this.features.find((item: any) => {
      const {
        name,
        alternative_names,
        alternative_names2,
        alternative_names3,
        coords,
      } = item.properties;
      const normalizedSearchTerm = searchTerm.trim().toLowerCase();
      const normalizedName = name.trim().toLowerCase();
      const normalizedAlternativeNames = alternative_names.trim().toLowerCase();
      const normalizedAlternativeNames2 = alternative_names2
        .trim()
        .toLowerCase();
      const normalizedAlternativeNames3 = alternative_names3
        .trim()
        .toLowerCase();
      const normalizedAlternativeNames4 = coords
        .toString()
        .trim()
        .toLowerCase();

      return (
        normalizedName === normalizedSearchTerm ||
        normalizedAlternativeNames === normalizedSearchTerm ||
        normalizedAlternativeNames2 === normalizedSearchTerm ||
        normalizedAlternativeNames3 === normalizedSearchTerm ||
        normalizedAlternativeNames4 === normalizedSearchTerm
      );
    });

    // return feature ? feature.geometry.coordinates : null;
    if (feature) {
      console.log('Coordinates found:', feature.geometry.coordinates);
      const convertedCoordinates = feature.geometry.coordinates.map(
        (coord: any) => parseFloat(coord)
      );
      console.log('convertedCoordinates', convertedCoordinates);
      this.view = new ol.View({
        center: convertedCoordinates,
        zoom: 17,
      });

      this.map.setView(this.view);

      const popupInfo = `<b style="color: white; background-color: white">${this.locationName}</b>`;

      // this.markerFeature = new Feature({
      //   type: 'icon',
      //   geometry: new Point(convertedCoordinates),  
      // });
      // const iconStyle = new Style({
      //   image: new Icon({
      //     // anchor: [0.5, 46],
      //     // anchorXUnits: 'fraction',
      //     // anchorYUnits: 'pixels',
      //     src: '../assets/icons/Robbery.png',
      //   }),
      // });
  
      // this.markerFeature.setStyle(iconStyle);

      // const markerSource = new VectorSource({
      //   features: [this.markerFeature],
      // });
  
      // const markerLayer = new VectorLayer({
      //   source: markerSource,
      // });
  
      // this.map.addLayer(markerLayer);
  
      this.fixedElementMarker.push(this.markerFeature);

      return feature.geometry.coordinates;

    } else {
      console.log('Coordinates not found for search term:', searchTerm);

      return null;
    }
  }

  startTimeline() {
    this.openTable=true;
    this.showbarstart=true;
    this.showbarreverse=false;
    this.ImgFirstCoord=[this.CdrData.CDR[0].IMSILocation[0][1],this.CdrData.CDR[0].IMSILocation[0][2]];
    this.ShowTimeline = true;
    this.Datatable=[];
    this.Datatablereverse=[];

    this.Datatable1=[...this.Datatable1.slice(0, this.Datatable1.length-this.index)];
    console.log("11111111111111111--------",this.Datatable1);
    console.log("2222222222222222--------",this.Datatable1.length);
    this.index=0;
    console.log('this.CdrData.CDR[0]>>>>>>>>>>>>>',this.CdrData.CDR[0]);
    console.log('starttimeline>>>>>>>>>>>>>');

    this.isRunning = true;
 
    // this.featurestcd=this.sectorarray['or'][0]['values_'].source.featureChangeKeys_;

    $('#tabledatabtn').css('display', '');
    $('#hidesimul').click();
    console.log('this.vectorLayerSector--------', this.vectorLayerSector);
    this.startLoop();
    let length=this.vectorLayerSector.getSource().getFeatures().length;
    console.log('length--------', length);
    
  }
  ReverseTimeline() {
    this.ShowTimeline = true;
    this.showbarreverse = true;
    this.showbarstart=false;
    this.Datatable=[];
    console.log('starttimeline>>>>>>>>>>>>>');

    this.isRunning = true;

    this.reverseloop();
  }

  startLoop() {
    let imsilocation:any=this.GroupIMSI(this.CdrData.CDR[0].IMSILocation);
 

    console.log('imsilocation',imsilocation);

 
    console.log('this.isRunning-----',this.isRunning);
    console.log('this.indexTimeline-----',this.indexTimeline);
    console.log('imsilocation.length-----',imsilocation.length);
    if (
      !this.isRunning || this.indexTimeline >= imsilocation.length
    )
     {
      console.log('Loop finished.');
     if (!this.isRunning || this.indexTimeline >= imsilocation.length  )
        {
        Swal.fire({
          text: 'Timeline Finished!!!',
          icon: 'warning',
          // showCancelButton: true,
        });
      }

      return;
    }
    // let xElement = this.CdrData.CDR[0].IMSILocation[this.indexTimeline ];
    let xElement = imsilocation[this.indexTimeline];
    console.log('xElement>>>', xElement);

   

    this.featuresSector = this.vectorLayerSector.getSource().getFeatures();

    let findedSector2: any = this.featuresSector.filter((sector: any) => {
      
      console.log('valuessssss------', sector.values_);
      sector = sector.values_;
      return (
        sector.Azimuth.toString() === xElement[0][2]
       && sector.lng.toString()  === xElement[0][1] 
       && sector.lat.toString()  === xElement[0][0]
      );
    });


    console.log('findedSector2-------------', findedSector2);
    console.log('[0000000]-------------', findedSector2[0]);

    
    for(let i =0;i<xElement[1].length;i++){
      console.log('xElement[1][i]',xElement[1][i]);
      let xElement2 = this.CdrData.CDR[0].IMSILocation[this.indexTimeline];
      console.log("xElement2---------",xElement2);
      console.log("date---------",this.dateTtoDate(xElement2[0]));

        let x = {
          Time: this.dateTtoDate(xElement[1][i]),
          event: 'calls',
          Lng: xElement[0][1],
          lat: xElement[0][0]
        }
        console.log('x1111111111111111111111', x);
        this.tablerow++;
        this.Datatable1.unshift(x);
        this.Datatable=this.Datatable1;
        //  this.Datatable.unshift(x);
         
        // this.Datatable=x;
        console.log('in map2---------',this.Datatable);
        $('#firstpage').click();

      }
      if (findedSector2.length > 0) {
      for (let j = 0; j < 5; j++) {
        setTimeout(() => {
          console.log('j>>>', j);
          if (j == 0) {
            console.log('on when j =0>>>', j);

            const fillColor = 'green';

            let x: any = new Style({
              fill: new Fill({ color: this.currentSectorColor }),
            });

            findedSector2[0].setStyle(x); // Assign the custom style function
            console.log('000000000----------', findedSector2[0]);
            console.log('000000000styleeeeee----------', findedSector2[0].style_);
          } else {
            if (findedSector2[0].style_.fill_.color_ === 'green') {
              // Sector is currently "on", turn it "off"
              const fillColor = 'white';

              let x: any = new Style({
                fill: new Fill({ color: fillColor }),
              });

              findedSector2[0].setStyle(x); // Assign the custom style function
            } else {
              // Sector is currently "off", turn it "on"
              console.log('on>>>', j);

              const fillColor = 'green';

              let x: any = new Style({
                fill: new Fill({ color: this.currentSectorColor }),
              });

              findedSector2[0].setStyle(x); // Assign the custom style function
            }
          }
        }, 250 * j); // This will delay each iteration by 2
       }
      this.sectorResetArray=findedSector2;

      let linestring:number=0;
      // linestring=this.ImgFirstCoord.distanceTo(L.latLng(xElement[0][0],xElement[0][1]));
 
      let line = new LineString([this.ImgFirstCoord,[xElement[0][0],xElement[0][1]]]);
      let distance = line.getLength()*100000;
      let output = Math.round(distance * 100) / 100;
      console.log("start:::::",this.ImgFirstCoord);
      console.log("endddd:::::",[xElement[0][0],xElement[0][1]]);
      console.log("line:::::::::::::::::",line);
      console.log("distance:::::::::::::::::",distance);
      console.log("output:::::::::::::::::",output);

      if(this.indexTimeline==1){
        this.view = new ol.View({
          center: [findedSector2[0].values_.lng, findedSector2[0].values_.lat],
          zoom: 15,
        });
  
        this.map.setView(this.view);
      
      }else{ 
        if(output<2472){
      
        }else{
          this.ImgFirstCoord=[xElement[0][0],xElement[0][1]];

          this.view = new ol.View({
            center: [findedSector2[0].values_.lng, findedSector2[0].values_.lat],
            zoom: 15,
          });
    
          this.map.setView(this.view);
      
        }
      }
 

      this.selectedTime = xElement[0];
    }

    setTimeout(async () => {
      console.log('speedTime', this.speedTime);
      this.indexTimeline = this.indexTimeline + 1;
      await this.startLoop();
    }, 4000 / this.speedTime);
  }

  // reverseloop() {
  //   let imsilocation:any=this.GroupIMSI(this.CdrData.CDR[0].IMSILocation);
  //   if (!this.isRunning || this.indexTimeline >= imsilocation.length) {
  //     console.log('Loop finished.');
  //     if (this.indexTimeline >= imsilocation.length) {
  //       Swal.fire({
  //         text: 'Reverse Timeline Finished!!!',
  //         icon: 'warning',
  //         // showCancelButton: true,
  //       });
  //     }

  //     return;
  //   }

 
  //   let xElement = imsilocation[this.indexTimeline];

  //   this.featuresSector = this.vectorLayerSector.getSource().getFeatures();


  //   let findedSector2: any = this.featuresSector.filter((sector: any) => {
      
  //     console.log('valuessssss------', sector.values_);
  //     sector = sector.values_;
  //     return (
  //       sector.Azimuth.toString() === xElement[0][2]
  //      && sector.lng.toString()  === xElement[0][1] 
  //      && sector.lat.toString()  === xElement[0][0]
  //     );
  //   });

  //   console.log('findedSector2', findedSector2);

  //   if (findedSector2.length > 0) {
  //     this.view = new ol.View({
  //       center: [findedSector2[0].values_.lng, findedSector2[0].values_.lat],
  //       zoom: 16,
  //     });
  //     this.map.setView(this.view);
  //     this.selectedTime = xElement[0];
  //   }

  //   setTimeout(async () => {
  //     console.log('speedTime', this.speedTime);
  //     this.indexTimeline = this.indexTimeline - 1;
  //     console.log('indexTimeline', this.indexTimeline);

  //     await this.reverseloop();
  //   }, 4000 / this.speedTime);
  // }

  reverseloop() {
    let imsilocation:any=this.GroupIMSI(this.CdrData.CDR[0].IMSILocation);

    if (!this.isRunning || this.indexTimeline < 1) {
      console.log('Loop finished.');
      if (this.indexTimeline < 1) {
        Swal.fire({
          text: 'Reverse Timeline Finished!!!',
          icon: 'warning',
          // showCancelButton: true,
        });
      }

      return;
    }

    let xElement = imsilocation[this.indexTimeline];

    this.featuresSector = this.vectorLayerSector.getSource().getFeatures();

    let findedSector2: any = this.featuresSector.filter((sector: any) => {
      
      console.log('valuessssss------', sector.values_);
      sector = sector.values_;
      return (
        sector.Azimuth.toString() === xElement[0][2]
       && sector.lng.toString()  === xElement[0][1] 
       && sector.lat.toString()  === xElement[0][0]
      );
    });


    console.log('findedSector2', findedSector2);
    for(let i =0;i<xElement[1].length;i++){
    console.log('xElement[1][i]',xElement[1][i]);

      let x = {
        Time: this.dateTtoDate(xElement[1][i]),
        event: 'calls',
        Lng: xElement[0][1],
        lat: xElement[0][0]
      }
      console.log('x', x);
      this.index++;
      this.tablerow++;
      this.Datatablereverse.unshift(x);
      this.Datatable=this.Datatablereverse;
      //  this.Datatable.unshift(x);
       $('#firstpage').click();
       console.log("Datatablereverse----------",this.Datatablereverse);
    }
 
    if (findedSector2.length > 0) {
      for (let j = 0; j < 5; j++) {
        setTimeout(() => {

          console.log('j>>>', j);
          if (j == 0) {
            console.log('on when j =0>>>', j);

            findedSector2[0].setStyle({
              fillOpacity: 0.7,
              opacity: 0.7,
              color: 'blue',
              fillColor: 'blue',
            });
          } else {
            if (findedSector2[0].style_.color_ === 'green') {
              // Sector is currently "on", turn it "off"
              console.log('on>>>', j);

              const fillColor = '#7eb2fb40';

              let x: any = new Style({
                fill: new Fill({ color: fillColor }),
                stroke: new Stroke({
                  color: '#007FFF',
                  width: 3,
                }),
              });

              findedSector2[0].setStyle(x)
            } else {
              // Sector is currently "off", turn it "on"
              console.log('on>>>', j);

              const fillColor = '#7eb2fb40';

              let x: any = new Style({
                fill: new Fill({ color: fillColor }),
                stroke: new Stroke({
                  color: '#007FFF',
                  width: 3,
                }),
              });

              findedSector2[0].setStyle(x)
            }
          }


        }, (250 * j) / this.speedTime); // This will delay each iteration by 2

      }
      let line = new LineString([this.ImgFirstCoord,[xElement[0][0],xElement[0][1]]]);
      let distance = line.getLength()*100000;
      let output = Math.round(distance * 100) / 100;
      console.log("start:::::",this.ImgFirstCoord);
      console.log("endddd:::::",[xElement[0][0],xElement[0][1]]);
      console.log("line:::::::::::::::::",line);
      console.log("distance:::::::::::::::::",distance);
      console.log("output:::::::::::::::::",output);

if(this.indexTimeline==1){
  this.view = new ol.View({
    center: [findedSector2[0].values_.lng, findedSector2[0].values_.lat],
    zoom: 15,
  });

  this.map.setView(this.view);
}else{ 
  if(output<2472){

  }else{
    this.ImgFirstCoord=[xElement[0][0],xElement[0][1]];

          this.view = new ol.View({
            center: [findedSector2[0].values_.lng, findedSector2[0].values_.lat],
            zoom: 15,
          });
    
          this.map.setView(this.view);
  }
}

      this.selectedTime = xElement[0];


    }


    setTimeout(async () => {
      console.log('speed', this.speedTime)
      this.indexTimeline = this.indexTimeline - 1;
      console.log('indexTimeline', this.indexTimeline)

      await this.reverseloop();
    }, 4000 / this.speedTime);
  }

  stopTimeline() {
    this.isRunning = false;
    // clearInterval(this.interval);
  }
  restTimeline(){
    $('#tabletest').css('display', 'none');
    this.showbarstart=false;
    this.showbarreverse = false;

    this.speedTime=1;
    this.Datatable=[];
    this.isRunning=false;
    this.indexTimeline=1;

    this.openTable=false;
    let x: any = new Style({
      fill: new Fill({
        color: '#7eb2fb40',
      }),
      stroke: new Stroke({
        color: '#007FFF',
        width: 3,
      }),
    });

    this.featuresSector.forEach((elt:any)=>{
      elt.setStyle(x);
    });
    this.featuresSector =[];
  }

  GroupIMSI(data:any){
    // Assuming your original object is named 'originalObject'
  
    
  const transformedData: any[] = [];
  let currentGroup: any[] | null = null;
  
  data.forEach((item:any) => {
    const key = item.slice(1, 4).join(',');
  
    if (currentGroup && currentGroup[0].join(',') === key) {
      // If the current group matches the key, add the first element to the existing group
      currentGroup[1].push(item[0]);
    } else {
      // If there's no current group or the key doesn't match, create a new group
      currentGroup = [item.slice(1, 4), [item[0]]];
      transformedData.push(currentGroup);
    }
  });
  console.log('data transformed>>>',transformedData)
  return transformedData;
  
  
  }

  clearsectors(event: any) {
    if (event == false) {
      // for (const layer of this.featuresSector) {
      //   this.map.removeLayer(layer);
      // }
      this.map.removeLayer(this.vectorLayerSector);
    } else {
      this.map.addLayer(this.vectorLayerSector);

      // this.vectorLayerSector.getSource().clear();
    //   this.featuresSector.forEach((element: any, key: any) => {
    //     // element.addTo(this.map);

    //     var vectorSource = new VectorSource({
    //       features: new GeoJSON().readFeatures(element),
    //     });

    //     const vectorLayer: any = new VectorLayer({
    //       source: vectorSource,
    //     });

    //     this.map.addLayer(vectorLayer);
    //   });
     }
  }

  clearTechnology(eventData: any) {
    let Technology = eventData.technology
 
    let event = eventData.event;
 let  arr:any[]= this.vectorLayerSector['values_']['source']['featureChangeKeys_'];
 const arrayOfObjects = Object.values(arr).flat();
 console.log('arrayOfObjects------------------',arrayOfObjects);
    if (event.target.checked == false) {
    this.vectorLayerSector.getSource().getFeatures().forEach( (feature:any) => {
      if (feature['values_'].Technology === Technology) {    
        this.removesectors.push(feature);
      }
      if (feature['values_'].Technology === Technology) {
        this.vectorLayerSector.getSource().removeFeature(feature);
    
      }
    });
    } else {
      this.removesectors.forEach( (feature)=> {
        this.vectorLayerSector.getSource().addFeature(feature); // Add each removed polygon back to the layer
    });
    this.removesectors=[];
    }
  }

  sectorCellIdchange(eventData: any){
    let eventcgi_id = eventData.cdrType;
    let event = eventData.event;
 console.log('eventData------------------',eventData);
 console.log('this.vectorLayerSector------------------',this.vectorLayerSector);




 let  arr:any[]= this.vectorLayerSector['values_']['source']['featureChangeKeys_'];
 const arrayOfObjects = Object.values(arr).flat();
 console.log('arrayOfObjects------------------',arrayOfObjects);
          if (eventcgi_id == 'mtc')
{     
  if (event.target.checked == false) {
      this.vectorLayerSector.getSource().getFeatures().forEach( (feature:any) => {
        feature['values_'].SINFO.forEach((elt:any)=>{
          if (elt[6].toString().startsWith("4503")){
            this.removesectors.push(feature);
          this.vectorLayerSector.getSource().removeFeature(feature);
          }
        });
      });
      this.removesectors = [...new Set(this.removesectors)];
      } else {
        this.removesectors.forEach( (feature)=> {
          this.vectorLayerSector.getSource().addFeature(feature);  
      });
      this.removesectors=[];
      }
    }else if (eventcgi_id == 'alfa'){
  if (event.target.checked == false) {
    this.featuresSector = this.vectorLayerSector.getSource().getFeatures();
    this.vectorLayerSector.getSource().getFeatures().forEach( (feature:any) => {
      feature['values_'].SINFO.forEach((elt:any)=>{
        if (!elt[6].toString().startsWith("4503")){
        this.vectorLayerSector.getSource().removeFeature(feature);
        this.removesectors.push(feature);
        }
      });
    });
    this.removesectors = [...new Set(this.removesectors)];
    } else {
      this.removesectors.forEach( (feature)=> {
        this.vectorLayerSector.getSource().addFeature(feature);  
    });
    this.removesectors=[];
    }
    }
  }

  clustersZoom(obj: any) {
    var desiredCoordinates = [
      obj.markerPositions[0][3],
      obj.markerPositions[0][4],
    ];
    console.log('zoomCordinatessss:::::::' + desiredCoordinates);
    var desiredZoomLevel = 0;
    if (this.coordinatesArray.length > 1) {
      desiredZoomLevel = 10;
    } else {
      desiredZoomLevel = 15;
    }

    this.map.getView().setZoom(desiredZoomLevel);
    this.map.getView().setCenter(desiredCoordinates);
  }



  saveBulkdrawRaduis(cRdaius: any) {
    this.closeAll();

    this.cRdaius = cRdaius;

    this.cRdaius = parseInt(this.cRdaius);

    if (this.cRdaius >= this.circleLimit) {
      Swal.fire({
        text: 'Circle limit exeeded!',
        backdrop: false,
        // showCancelButton: true,
      });
    }

    console.log('fixedMarker---------------');

    console.log('this.fixedMarkersArray.------', this.fixedMarkersArray.length);

    // console.log('this.CdrData[0]------', this.CdrData[0].length);
    console.log('.length------', this.fixedMarkersArray.length);

    if (JSON.parse(localStorage.getItem('fixedelementsObject')!) != null) {
     

      let fixedelementsObject = JSON.parse(
        localStorage.getItem('fixedelementsObject')!
      );
      fixedelementsObject.forEach((element: any, key: any) => {
        console.log('center>>>', [element[3], element[4]]);
        this.displayCircle(
          element[3],
          element[4],
          this.cRdaius,
          this.BulkshapeId
        );
        this.BulkshapeId++;
      });
    } else {


      if (this.fixedMarkersArray.length > 0 && this.CdrData[0].length === 0) {
     
        this.fixedMarkersArray.forEach((element: any, key: any) => {
          let long = element.geometryChangeKey_.target.flatCoordinates[0];
          let lat = element.geometryChangeKey_.target.flatCoordinates[1];
          this.displayCircle(long, lat, this.cRdaius, this.BulkshapeId);
          this.BulkshapeId++;
        });
      } else if (
        this.CdrData[0].length > 0 &&
        this.fixedMarkersArray.length === 0
      ) {
     
        this.CdrData[0].forEach((element: any, key: any) => {
          console.log('element-----', element);
          console.log('BTS-----', element.BTS);
          let long = element.BTS.LONGITUDE;
          let lat = element.BTS.LATITUDE;
          console.log('long-----', long);
          console.log('lat-----', lat);
          this.displayCircle(long, lat, this.cRdaius, this.BulkshapeId);
          this.BulkshapeId++;
        });
      } else if (
        this.fixedMarkersArray.length > 0 &&
        this.CdrData[0].length > 0
      ) {

        this.fixedMarkersArray.forEach((element: any, key: any) => {
          let long = element.geometryChangeKey_.target.flatCoordinates[0];
          let lat = element.geometryChangeKey_.target.flatCoordinates[1];
          this.displayCircle(long, lat, this.cRdaius, this.BulkshapeId);
        });

        this.CdrData[0].forEach((element: any, key: any) => {
          console.log('element-----', element);
          console.log('BTS-----', element.BTS);
          let long = element.BTS.LONGITUDE;
          let lat = element.BTS.LATITUDE;
          console.log('long-----', long);
          console.log('lat-----', lat);
          this.displayCircle(long, lat, this.cRdaius, this.BulkshapeId);
         
        });
        this.BulkshapeId++;
      }
    }
  }

  async displayCircle(long: any, lat: any, radius: any, BulkshapeId: any) {
    const view = this.map.getView();

    console.log('centerrrrrrrrrrrrrrrrrrrrrrrrrrrrrr', [long, lat]);

    const resolution = view.getResolution();
    console.log('resolution', resolution);
    if (resolution === undefined) {
      console.error('Resolution is undefined');
      return;
    }
    const radiusInPixels = radius;
    const element3 = parseFloat(long);
    const element4 = parseFloat(lat);
    this.circleFeature = new ol.Feature(
      new circle(
        transform([element3, element4], 'EPSG:4326', 'EPSG:3857'),
        radiusInPixels * 1.24
      ).transform('EPSG:3857', 'EPSG:4326')
    );
     
    console.log('radiusssssssssss', radius);
    console.log('coordinatesaraaayyyyyyyy', this.coordinatesArray);

    const circleStyle = new Style({
      fill: new Fill({
        color: '#7eb2fb40',
      }),
      stroke: new Stroke({
        color: '#007FFF',
        width: 3,
      }),
    });
    this.circleFeature.setStyle(circleStyle);
 

    this.circleLayer = new VectorLayer({
      source: new VectorSource({
        features: [this.circleFeature],
      }),
      zIndex: 0,
    });
    console.log('00000000000000000', this.circleLayer);    
    this.map.addLayer(this.circleLayer);
    this.circles.push(this.circleLayer);
    console.log('11111111111111', this.circleLayer);    

    if (this.coordinatesArray.find((a) => a.ID === BulkshapeId)) {
    } else {
      const circleGeometry = this.circleFeature.getGeometry();

      console.log("circleGeometry:", circleGeometry);

      const polygon_extent = circleGeometry.getExtent();
      
      // Log the extent for debugging
      console.log("polygon_extent:", polygon_extent);
      
      // Process features intersecting the circle extent
      this.vectorSourceCountry.forEachFeatureIntersectingExtent(polygon_extent, (feature:any) => {
        console.info(feature);
        console.info('iso:', feature.values_.iso_a2);
        let iso = feature.values_.iso_a2;
        this.countries.push(iso);
        this.countries = [...new Set(this.countries)];
      });
  //     var polygon_extent = event.feature.getGeometry()!.getExtent();
  //     // console.log("polygon_extent-----------",polygon_extent);
  //     console.log("polygon_extent.-------------",polygon_extent);

  //     this.vectorSourceCountry.forEachFeatureIntersectingExtent(polygon_extent,  (feature:any) => {
  //       console.info(feature);
  //       console.info('isoo---------',feature.values_.iso_a2);
  //       let iso=feature.values_.iso_a2;
  //       this.countries.push(feature.values_.iso_a2);
  //       this.countries= [...new Set(this.countries)];
  //     });
  //       console.log("this.countries.-------------",this.countries);

        
    let  C_countryCodes:any;

   await  this.datacrowdService.getcountry(this.countries).then((ress:any)=>{
      console.log('getcountry>>>>',ress);
      // C_subregion=ress[0];
      // C_region=ress[1];
      // C_Country=ress[2];
      C_countryCodes=ress;
      C_countryCodes=this.convertCountryCode2(C_countryCodes)

    }) 
    
      this.coordinatesArray.push({
        Bounds: '',
        ID: BulkshapeId,
        Name: '',
        PolyBoundsCoords: '',
        Type: 'Circle',
        Value: '',
        center: { lng: element3, lat: element4 },
        ol_uid: BulkshapeId,
        radius: radiusInPixels,
        selectedStartDate: '',
        selectedEndDate: '',
        leafletid: BulkshapeId,
        countrycodes:C_countryCodes
      });
    }
    
  

    console.log('circles in displaycircle:::::', this.circles);
    console.log('2222222222222', this.circleLayer);    
 
    this.displayCircleForLoop(long, lat, this.cRdaius, this.BulkshapeId);

  }

  displayCircleForLoop(long: any, lat: any, radius: any, BulkshapeId: any) {
 
    const view = this.map.getView();

    console.log('centerrrrrrrrrrrrrrrrrrrrrrrrrrrrrr', [long, lat]);

    const resolution = view.getResolution();
    console.log('resolution', resolution);
    if (resolution === undefined) {
      console.error('Resolution is undefined');
      return;
    }
    const radiusInPixels = radius;
    const element3 = parseFloat(long);
    const element4 = parseFloat(lat);
 
    this.circleFeatureLoop = new ol.Feature(
      new circle(
        transform([element3, element4], 'EPSG:4326', 'EPSG:3857'),
        radiusInPixels * 1.24
      ).transform('EPSG:3857', 'EPSG:4326')
    );
    console.log('radiusssssssssss', radius);
    console.log('coordinatesaraaayyyyyyyy', this.coordinatesArray);

    const circleStyle = new Style({
      fill: new Fill({
        color: '#7eb2fb40',
      }),
      stroke: new Stroke({
        color: '#007FFF',
        width: 3,
      }),
    });
 
    this.circleFeatureLoop.setStyle(circleStyle);

   
    this.circleLayerLoop = new VectorLayer({
      source: new VectorSource({
        features: [this.circleFeatureLoop],
      }),
      zIndex: 0,
    });
    this.magnifiedMap.addLayer(this.circleLayerLoop);
    this.circlesLoop.push(this.circleLayerLoop);
    
    console.log('33333333333333', this.circleLayerLoop);
 
    
    console.log('4444444444444', this.magnifiedMap);

  }

  closeAll() {
    this.bulkdraw = false;
  }
  // bullseye() {
  //   console.log('fixedMarkersArray>><<<', this.fixedMarkersArray);
  //   console.log('this.CdrData[0].length>><<<',  this.CdrData[0].length);
  //   console.log('this.CdrData>><<<',  this.CdrData);
  //   console.log('this.CdrData[0]>><<<',  this.CdrData[0]);
  //    let type="";
  //   if (
  //     (this.fixedMarkersArray.length == 0)  ) {
  //       console.log("00000000000000");
  //       if(this.CdrData[0].length == 0 ){
  //       console.log("cdrr 00000000000");

  //         Swal.fire({
  //           text: 'Select 1 or more   Elements!',
  //           backdrop: false
  //           // showCancelButton: true,
  //         });
  //       }else{
  //         console.log("cdrr 1111111111111");

  //         type="cdr";
  //         Swal.fire({
  //           // title: 'Please enter circle radius',
  //           input: 'text',
  //           text: 'Please enter circle radius',
  //           showCancelButton: true,
  //           backdrop: false,
  //           confirmButtonText: 'Ok'
  //         }).then((result) => {
  //           if (result.isConfirmed)
  //         {
  //           this.saveBulkdrawRaduis(result.value,type)
  //         }
  //         });
  //       }

  //   }
  //   else {
  //     if(this.CdrData[0].length === 0)
  //   {
  //     alert('else');

  //   type="fixedMarker";
  //   Swal.fire({
  //     // title: 'Please enter circle radius',
  //     input: 'text',
  //     text: 'Please enter circle radius',
  //     showCancelButton: true,
  //     backdrop: false,
  //     confirmButtonText: 'Ok'
  //   }).then((result) => {
  //     if (result.isConfirmed)
  //   {
  //     this.saveBulkdrawRaduis(result.value,type) ;
  //   }
  //   });}

  //     if(this.CdrData[0].length > 0){
  //       alert('else   cdr  0000');
  //       type="cdr";
  //       Swal.fire({
  //         // title: 'Please enter circle radius',
  //         input: 'text',
  //         text: 'Please enter circle radius',
  //         showCancelButton: true,
  //         backdrop: false,
  //         confirmButtonText: 'Ok'
  //       }).then((result) => {
  //         if (result.isConfirmed)
  //       {
  //         this.saveBulkdrawRaduis(result.value,type);
  //         alert('next');
  //         type="fixedMarker";
  //         this.saveBulkdrawRaduis(result.value,type);
  //       }
  //       });
  //     }

  //   }
  // }

  bullseye() {
    console.log('fixedMarkersArray>><<<', this.fixedMarkersArray);
    // console.log('this.CdrData[0].length>><<<', this.CdrData[0].length);
    // console.log('this.CdrData>><<<', this.CdrData);
    // console.log('this.CdrData[0]>><<<', this.CdrData[0]);

    if (this.fixedMarkersArray.length == 0) {
      console.log('00000000000000');
      if (this.CdrData[0].length == 0) {
        console.log('cdrr 00000000000');

        Swal.fire({
          text: 'Select 1 or more   Elements!',
          backdrop: false,
          // showCancelButton: true,
        });
      } else {
        console.log('cdrr 1111111111111');
        Swal.fire({
          // title: 'Please enter circle radius',
          input: 'text',
          text: 'Please enter circle radius',
          showCancelButton: true,
          backdrop: false,
          confirmButtonText: 'Ok',
        }).then((result) => {
          if (result.isConfirmed) {
            this.saveBulkdrawRaduis(result.value);
          }
        });
      }
    } else {


      Swal.fire({
        // title: 'Please enter circle radius',
        input: 'text',
        text: 'Please enter circle radius',
        showCancelButton: true,
        backdrop: false,
        confirmButtonText: 'Ok',
      }).then((result) => {
        if (result.isConfirmed) {
          this.saveBulkdrawRaduis(result.value);
        }
      });
    }
  }
  getUniqueNumbers(numbersArray: number[]): number[] {
    // Use the 'Set' object to store unique numbers
    const uniqueNumbers = new Set<number>();

    // Iterate over the array and add each number to the set
    for (const number of numbersArray) {
      uniqueNumbers.add(number);
    }

    // Convert the set back to an array
    const uniqueNumbersArray = Array.from(uniqueNumbers);

    return uniqueNumbersArray;
  }

  imageExists(image_url: any) {
    var http = new XMLHttpRequest();
    http.open('HEAD', image_url, false);
    http.send();
    return http.status != 404;
  }

  displayFixedElements(object: any) {

    console.log('map>>>', this.map);
    this.fixedMarkersGroup = [];
    this.marker = [];
    const markerInstances: any = []; // Step 1: Define a variable to store the marker instances.
    let clusterFeatures: any = [];
    let src: any;
    let srcname:any;
    for (const layer of this.fixedMarkersGroupsArray) {
      this.map.removeLayer(layer);
    }
    //  console.log(" object element   ====",object)
    console.log('object------------>>>', object);
    const fixelementsString = object;
    // console.log("fixelementsString   ====",fixelementsString)
    //  console.log(" object element22   ====",object)
    for (let i = 0; i < object.length; i++) {
      this.marker = new Feature(
        new Point([parseFloat(object[i][3]), parseFloat(object[i][4])])
      );

      this.marker.A_name = object[i][1];
      this.marker.A_id = object[i][0];
      srcname=this.marker.A_Type = object[i][2];
      this.marker.A_lng = object[i][3];
      this.marker.A_lat = object[i][4];
      this.marker.set('type', 'fixedelement');
      clusterFeatures.push(this.marker); // Create a new array for each marker
      this.fixedMarkersArray.push(this.marker);
    }

    console.log('clusterFeatures>>>', clusterFeatures);
    this.source = new VectorSource({
      features: clusterFeatures,
    });
    
   
// Now you can work with the features obtained from the cluster source
console.log('Cluster Features:---------------',  this.source.getFeatures());
    console.log('source>>>', this.source);

    this.clusterSource = new Cluster({
      minDistance: 100,
      source: this.source,
    });
    console.log('clusterSource>>>>>>>>', this.clusterSource);
    const styleCache: { [key: number]: Style } = {};

    this.fixedMarkersGroup = new VectorLayer({
      source: this.clusterSource,
      zIndex: 9999,
      style: function (cluster) {
        // console.log("cluster:::::",cluster);
        const size = cluster.get('features').length;
        console.log('features0----------------::::',cluster.get('features'));
        console.log('name-------------::::',cluster.get('features')[0].get('A_Type'));
        console.log('name22222-------------::::',cluster.get('features')[0].A_Type);
        let style = styleCache[size];

        const html = '<div class="elementGroup">' + size + '</div>';
        if (!style) {
          if (size === 1) {
            
            src =
            '/cybercrowd/angular-offline/assets/icons/' +
            cluster.get('features')[0].A_Type +
            '.png'; // Access A_Type directly from the feature
    
          } else if (size > 1) {
            src = '/cybercrowd/angular-offline/assets/icons/elementGroup.png';
          }
          styleCache[size] = style;
        }

        return new Style({
          image: new Icon({
            src: src,
            scale: 0.8,
            // size:[32,32]
          }),
          text: new Text({
            text: size.toString(),
            fill: new Fill({ color: '#fff' }),
          }),
        });
      },
    });

    this.fixedMarkersGroup.set('type', 'fixedelement');

    this.map.addLayer(this.fixedMarkersGroup);

    this.fixedMarkersGroupsArray.push(this.fixedMarkersGroup);
    console.log("this.fixedMarkersGroup-----------",this.fixedMarkersGroup);      

    this.featuresSector =  this.source.getFeatures();


    // console.log("fixedMarkersArray in displayfixelementfunction:::",this.fixedMarkersArray);
    this.map.getView().setZoom(10);
    this.map
      .getView()
      .setCenter([object[object.length - 1][3], object[object.length - 1][4]]);

    console.log('map222>>>', this.map);
    console.log('mapppplayers >>>>>>>>>>>>> ', this.map.getLayers().getArray());
  }

  getDistinctNumbers(arr: number[]): number[] {
    // Use the Set object to remove duplicates
    const distinctNumbers = [...new Set(arr)];

    // Return the array of distinct numbers
    return distinctNumbers;
  }

  onTelColumnClicked(params: any) {
    // alert("in TelColumnClicked")
    // Access the row data for the clicked cell
    console.log('params>>>>>>>', params);
    const rowData = params.data.Tel;

    // Call your desired function or perform any action here
    (window.parent.parent.parent[7] as any).openkyc(rowData);
    //because one each click we add teh device to the multislection
    localStorage.clear();
  }

  openPopup2() {
    // this.grid=true;
    // this.value=true;
this.GridID='popupGrid';
    $('#popup2').css('display', 'none');

    var popup = this.elem_id('popupGrid');

    var olpopup = new ol.Overlay({
      element: popup!,
      autoPan: false,
    });
    olpopup.setPosition(this.GridPosition);

    this.map.addOverlay(olpopup);
    $('#popupGrid').css('display', '');
    return false;
  }

  opentcdGrid() {
    // this.grid=true;
    // this.value=true;
    this.GridID='TCDContent';
    $('#popup2').css('display', 'none');
    $('#popupGrid').css('display', 'none');
    $('#TCDContent').css('display', '');

    var popup = this.elem_id('TCDContent');

    var olpopup = new ol.Overlay({
      element: popup!,
      autoPan: false,
    });
    olpopup.setPosition(this.GridPosition);

    this.map.addOverlay(olpopup);
    return false;
  }

  displayPolygon(obj: any) {
    const polygonCoords = obj.PolyBoundsCoords.map((coord: any) => {
      const element3 = parseFloat(coord.lat);
      const element4 = parseFloat(coord.lng);
      // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
      return [element4, element3];
    });
    console.log('obj>>>>>>>>>', obj);

    console.log('polygonCoords', polygonCoords);

    console.log('coordobj>>>>>>>>>', obj.PolyBoundsCoords);

    const geojsonObject = {
      type: 'FeatureCollection',
      crs: {
        type: 'name',
        properties: {
          name: 'EPSG:3857',
        },
      },
      features: [
        {
          type: 'Feature',
          geometry: {
            type: 'Polygon',
            coordinates: [polygonCoords],
          },
        },
      ],
    };
    console.log('geojsonObject>>>>>>>>>', geojsonObject);

    const styles = [
      new Style({
        fill: new Fill({
          color: '#7eb2fb40',
        }),
        stroke: new Stroke({
          color: '#007FFF',
          width: 3,
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: '#7eb2fb',
          }),
        }),
      }),
      new Style({  
        geometry: (feature: any) => {
          const coordinates = feature.getGeometry().getCoordinates()[0];
          return new MultiPoint(coordinates);
        },
      }),
    ];
    if (this.coordinatesArray.find((a) => a.ID === obj.ID)) {
    } else {
 
      this.coordinatesArray.push({
        Bounds: polygonCoords,
        ID: obj.ID,
        Name: obj.Name,
        PolyBoundsCoords: polygonCoords,
        Type: obj.Type,
        Value: '',
        center: obj.center, // Set the center as 0,0 for polygons
        ol_uid: obj.leafletid,
        radius: '',
        selectedStartDate: '',
        selectedEndDate: '',
        leafletid: obj.leafletid,

        countrycodes:[]
      });
    }
    console.log('arraypolygon::::::::::>>>>>>>', this.coordinatesArray);
    console.log('styles::::::::::', styles);

    this.sourcePolygon = new VectorSource({
      features: new GeoJSON().readFeatures(geojsonObject),
    });
    this.sourcePolygonLoop = new VectorSource({
      features: new GeoJSON().readFeatures(geojsonObject),
    });
    console.log('source::::::::::', this.sourcePolygon);
    this.layerPolygon = new VectorLayer({
      source: this.sourcePolygon,
      style: styles,
    });
    this.polygons.push(this.layerPolygon);

    this.layerPolygonLoop = new VectorLayer({
      source: this.sourcePolygonLoop,
      style: styles,
    });
    this.polygonsLoop.push(this.layerPolygonLoop);


    console.log('layer::::::::::', this.layerPolygon);
    this.map.addLayer(this.layerPolygon);
    this.magnifiedMap.addLayer(this.layerPolygonLoop);
  }

  
  displayPolygonForLoop(obj: any) {
    const polygonCoords = obj.map((coord: any) => {
      const element3 = parseFloat(coord.lat);
      const element4 = parseFloat(coord.lng);
      // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
      return [element4, element3];
    });
    console.log('obj>>>>>>>>>', obj);

    console.log('polygonCoords', polygonCoords);

    

    const geojsonObject = {
      type: 'FeatureCollection',
      crs: {
        type: 'name',
        properties: {
          name: 'EPSG:3857',
        },
      },
      features: [
        {
          type: 'Feature',
          geometry: {
            type: 'Polygon',
            coordinates: [polygonCoords],
          },
        },
      ],
    };
    console.log('geojsonObject>>>>>>>>>', geojsonObject);

    const styles = [
      new Style({
        fill: new Fill({
          color: '#7eb2fb40',
        }),
        stroke: new Stroke({
          color: '#007FFF',
          width: 3,
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: '#7eb2fb',
          }),
        }),
      }),
      new Style({  
        geometry: (feature: any) => {
          const coordinates = feature.getGeometry().getCoordinates()[0];
          return new MultiPoint(coordinates);
        },
      }),
    ];
   
    this.sourcePolygonLoop = new VectorSource({
      features: new GeoJSON().readFeatures(geojsonObject),
    });

    this.layerPolygonLoop = new VectorLayer({
      source: this.sourcePolygonLoop,
      style: styles,
    });
    this.polygonsLoop.push(this.layerPolygonLoop);


    console.log('layer::::::::::', this.layerPolygon);
     this.magnifiedMap.addLayer(this.layerPolygonLoop);
  }

  displayPolyline(obj: any, type: string) {
    let color: any;
    const polylineCoords = obj.Bounds.map((coord: any) => {
      const element3 = parseFloat(coord.lat);
      const element4 = parseFloat(coord.lng);
      // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
      return [element4, element3];
    });
    if (type === 'simulation') {
      color = '#7eb2fb';
    } else if (type === 'direction') {
      color = '#0c1a10';
    } else if (type === 'directionByTime') {
      color = '#3388ff';
    }
    const styles = [
      new Style({
        fill: new Fill({
          color: color,
        }),
        stroke: new Stroke({
          color: color,
          width: 3,
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: color,
          }),
        }),
      }),
    ];
    if (this.coordinatesArray.find((a) => a.ID === polylineCoords)) {
    } else if (type === 'direction' || type === 'directionByTime') {
    } else {
      this.coordinatesArray.push({
        Bounds: polylineCoords,
        ID: obj.ID,
        Name: obj.Name,
        PolyBoundsCoords: polylineCoords,
        Type: obj.Type,
        Value: '',
        center: obj.center,
        ol_uid: obj.leafletid,
        radius: '',
        selectedStartDate: '',
        selectedEndDate: '',
        leafletid: obj.leafletid,
        countrycodes:[]
      });
    }
    this.sourcePolyline = new VectorSource({
      features: [new Feature(new LineString(polylineCoords))],
    });
    this.layerPolyline = new VectorLayer({
      source: this.sourcePolyline,
      style: styles,
    });
    this.polylines.push(this.layerPolyline);
    this.map.addLayer(this.layerPolyline);

    this.sourcePolylineLoop = new VectorSource({
      features: [new Feature(new LineString(polylineCoords))],
    });
    this.layerPolylineLoop = new VectorLayer({
      source: this.sourcePolylineLoop,
      style: styles,
    });
    this.polylinesLoop.push(this.layerPolylineLoop);
    this.magnifiedMap.addLayer(this.layerPolylineLoop);
  }

  displayPolylineForLoop(obj: any) {
    let color: any;
    const polylineCoords = obj.map((coord: any) => {
      const element3 = parseFloat(coord.lat);
      const element4 = parseFloat(coord.lng);
      // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
      return [element4, element3];
    });
   
      color = '#7eb2fb';
  
    const styles = [
      new Style({
        fill: new Fill({
          color: color,
        }),
        stroke: new Stroke({
          color: color,
          width: 3,
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: color,
          }),
        }),
      }),
    ];
    this.sourcePolylineLoop = new VectorSource({
      features: [new Feature(new LineString(polylineCoords))],
    });
    this.layerPolylineLoop = new VectorLayer({
      source: this.sourcePolylineLoop,
      style: styles,
    });
    this.polylinesLoop.push(this.layerPolylineLoop);
    this.magnifiedMap.addLayer(this.layerPolylineLoop);
  }


  closeGridPopup() {
    this.aggridmarker = false;
    this.rowData = [];
    this.value = false;

    $('#popupGrid').css('display', 'none');
    $('#popup2').css('display', 'none');
  }

  closeFixedElementsPopup() {
    $('#fixedelementsPopup').css('display', 'none');
  }

  CallFunctions(value: any) {
    console.log('value is : ', value);
    const functionName = value;
    const result = eval(`this.${functionName}()`);
  }

  saveChanges() {
    this.modifiedshapes.forEach((feature: any) => {
      console.log('coordinatearray>>>', this.coordinatesArray);
      console.log('featureeeeeeeeeeeee:', feature);
      let featureId =
        parseInt(feature.getProperties()?.['geometry'].ol_uid) + 1;
      console.log('iddddddddddddddddddddddddddddddddddddd:', featureId);

      let id: string = featureId.toString();
      var drawbElement = this.coordinatesArray.find((a) => a.ID === id);
      var drawbElementType = drawbElement!['Type'];

      if (drawbElementType === 'Circle') {
        this.centerCoordinates = feature
          .getProperties()
          ?.['geometry'].getCenter();
        console.log(
          'this.111111111111111111111::::::::::::::::::',
          feature.getProperties()?.['geometry']
        );
        console.log(
          'this.2222222222222222222::::::::::::::::::',
          feature.getProperties()
        );
        console.log('drawbElement::::::::::::::::::', drawbElement);

        console.log(
          'this.33333333333333333333333333::::::::::::::::::',
          this.centerCoordinates
        );

        this.radius = feature.getProperties()?.['geometry'].getRadius() * 90000;
        console.log('radius in meters:', this.radius);

        const sourceProjection = this.map.getView().getProjection();
        const targetProjection = 'EPSG:900913'; // commonly used projection for meters

        // Convert the radius from the source projection to meters
        this.radiusInMeters = transformExtent(
          [0, 0, this.radius, this.radius],
          sourceProjection,
          targetProjection
        )[2];

        this.lon = this.centerCoordinates[0];
        this.lat = this.centerCoordinates[1];
        let position = { lng: this.lon, lat: this.lat };
        this.map.addInteraction(select);
        drawbElement!.radius = this.radiusInMeters;
        drawbElement!.center = position;
        let latesttttt = feature.getGeometry().clone();
        console.log('latestttttol_uid::::::;', latesttttt['ol_uid']);

        latesttttt['ol_uid'] = featureId.toString();
        console.log('latesttttt22222222222222222222222::::::;', latesttttt);
        let shape = this.firstmodifiedshapes.find((a: any) => a.ol_uid === id);
        console.log(
          'shape11111111111111111111111111111111::::::;',
          shape.getRadius()
        );

        this.firstmodifiedshapes.find((a: any) => a.ol_uid === id) !=
          latesttttt;

        console.log(
          'shape22222222222222222::::::;',
          this.firstmodifiedshapes.find((a: any) => a.ol_uid === id).getRadius()
        );
      } else if (drawbElementType === 'Polygon') {
        this.centerCoordinates = feature.geometryChangeKey_.target;
        console.log(
          'this.centerCoordinates::::::::::::::::::',
          this.centerCoordinates
        );
        console.log('this.feature::::::::::::::::::', feature.getGeometry());
        console.log(
          'this.centerrrrrrrrrrrrrr::::::::::::::::::',
          feature.getGeometry()
        );

        this.coordinates = feature.getGeometry().getCoordinates()[0];
        console.log('coordinate polygon:', this.coordinates);
        const geometry = feature.getGeometry();
        console.log('geometry polygon:', geometry);

        const polygon = new Polygon(geometry.getCoordinates());

        const area = getArea(polygon) * 10000;

        let output;
        output = Math.round(area * 100000000) / 100;
 
        const points = [];
        for (let i = 0; i < this.coordinates.length; i++) {
          const vertex = this.coordinates[i];
          const [lng, lat] = vertex;
          points.push({ lng, lat });
        }

        const centroid = this.calculateCentroid(points);

        drawbElement!.Bounds = points;
        drawbElement!.PolyBoundsCoords = points;
        drawbElement!.center = centroid;
        console.log('drawbElementpolygonnnnnnnnnnn::::::', centroid);

        console.log('drawbElementpolygonnnnnnnnnnn::::::', drawbElement);
        this.map.addInteraction(select);
      } else if (drawbElementType === 'Polyline') {
        this.coordinates = feature.getGeometry().getCoordinates();
        this.length =
          feature
            .getGeometry()
            .getLength({ projection: this.map.getView().getProjection() }) *
          100000;
        const points = [];
        for (let i = 0; i < this.coordinates.length; i++) {
          const vertex = this.coordinates[i];
          const [lng, lat] = vertex;
          points.push({ lng, lat });
        }
        console.log('points::::::', points);

        drawbElement!.Bounds = points;
        drawbElement!.PolyBoundsCoords = points;

        console.log('drawbElementpolygonnnnnnnnnnn::::::', drawbElement);
        this.map.addInteraction(select);
      }
    });

    this.editMode = false;
    this.map.removeInteraction(this.select);
    this.map.removeInteraction(this.modify);
  }

  EditShapes() {
    this.editMode = true;
    this.modify = new Modify({
      source: this.vectorSource,
    });
    this.map.addInteraction(this.modify);
    console.log('vectorsourceeeeeeeeeeeeeee:', this.vectorSource.getFeatures());

    let type: any;
    let i: any;

    this.modify.on('modifystart', (event: any) => {
      this.feature = event.features.getArray()[0];
      i = event.features.getArray()[0].ol_uid;

      type = event.features.getArray()[0].getGeometry().getType();

      this.modifiedFeature = event.features.getArray()[0]; // Assuming you are modifying a single feature
      this.originalFeature = this.modifiedFeature.getGeometry().clone();

      if (
        this.modifiedshapes.filter((elt: any) => elt.ol_uid === i.toString())
          .length == 0
      ) {
        this.firstshape = this.modifiedFeature.getGeometry().clone();
        console.log('firstshape::::::;', this.firstshape);
        this.firstshape.ol_uid = event.features.getArray()[0].ol_uid;
        this.firstmodifiedshapes.push(this.firstshape);
      }
    });

    this.modify.on('modifyend', (event: any) => {
      let f = event.features.array_;
      let shape = this.firstmodifiedshapes.find(
        (a: any) => a.ol_uid === f[0].ol_uid.toString()
      );
      console.log('f[0]:', f[0].getGeometry().getRadius());
      if (type === 'Circle') {
        let radius: any = f[0].getGeometry().getRadius() * 90000;

        if (radius > this.circleLimit) {
          Swal.fire({
            text: 'Circle radius is too big',
            backdrop: false,
          });

          f[0].setGeometry(shape);
        }
      } else if (type === 'Polygon') {
        const geometry = f[0].getGeometry();
        console.log('geometry:::', geometry);

        const polygon = new Polygon(geometry.getCoordinates());

        const area = getArea(polygon) * 10000;
        console.log('area:::::', area);

        let output;
        output = Math.round(area * 100000000) / 100;
        console.log('formatttareaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', output);

        if (output > this.polygoneLimit) {
          Swal.fire({
            text: 'Polygon limit exceeded',
            backdrop: false,
          });
          this.deactivateDrawInteraction();
          f[0].setGeometry(shape);
        }
      } else if (type === 'LineString') {
        let coordinates = f[0].getGeometry().getCoordinates();
        let length =
          f[0]
            .getGeometry()
            .getLength({ projection: this.map.getView().getProjection() }) *
          100000;
        console.log('lengthhhhhhhhhhhhhhhhhh:', length);
        const points = [];
        if (length > parseInt(this.polygoneLimit)) {
          Swal.fire({
            text: 'Polyline limit exceeded',
            backdrop: false,
          });
          this.deactivateDrawInteraction();
          f[0].setGeometry(shape);
        }
      }
      this.modifiedshapes.push(this.modifiedFeature);
      console.log('77777777777777777777777777777777 :', this.modifiedshapes);
      console.log(
        '8888888888888888888888888 :',
        this.modifiedshapes.filter((elt: any) => elt.ol_uid === i.toString())
          .length
      );
    });

    this.status = true;
  }
  cancelEdit() {
    // this.vectorSource.getFeatures()
    console.log('this.modifiedshapes:', this.modifiedshapes);
    this.modifiedshapes.forEach((elt: any) => {
      console.log('iddddddddddddddddddddddddd:', elt.ol_uid);
      console.log('00000000000000000000:', this.firstmodifiedshapes);
      this.firstmodifiedshapes.find((a: any) =>
        console.log('insodeeeee::::', a.ol_uid)
      );
      console.log(
        'hiiiiiiiiiiiiiiiiiiiiiiii:',
        this.firstmodifiedshapes.find(
          (a: any) => a.ol_uid === elt.ol_uid.toString()
        )
      );
      let shape = this.firstmodifiedshapes.find(
        (a: any) => a.ol_uid === elt.ol_uid.toString()
      );
      elt.setGeometry(shape);
    });
    this.editMode = false;
    this.modify.setActive(false);
    this.map.removeInteraction(this.select);
    this.map.removeInteraction(this.modify);
  }

  // cancelEdit(){
  //   // this.vectorSource.getFeatures()
  //   console.log("this.modifiedshapes:",this.modifiedshapes);
  //   this.modifiedshapes.forEach((elt:any)=>{
  //    console.log("iddddddddddddddddddddddddd:",elt.ol_uid);
  //   console.log("00000000000000000000:",this.firstmodifiedshapes);
  //   this.firstmodifiedshapes.find((a:any)=>console.log("insodeeeee::::",a.ol_uid));
  //   console.log("hiiiiiiiiiiiiiiiiiiiiiiii:",this.firstmodifiedshapes.find((a:any)=>a.ol_uid===elt.ol_uid.toString()));
  //   let shape=this.firstmodifiedshapes.find((a:any)=>a.ol_uid===elt.ol_uid.toString());
  //  elt.setGeometry(shape);
  //   });
  //   // this.editMode = false;
  //   // this.modify.setActive(false);
  //   // this.map.removeInteraction(this.select);
  //   // this.map.removeInteraction(this.modify);
  // }

  findDataById(
    idToFind: number
  ): { selectedStartDate: string; selectedEndDate: string } | null {
    const foundData = this.DatingObj.find((item) => item.id === idToFind);
    return foundData
      ? {
          selectedStartDate: foundData.selectedStartDate,
          selectedEndDate: foundData.selectedEndDate,
        }
      : null;
  }

  async closepopup() {
    $('#datepicker').css('display', 'none');
    // $("#datepicker").remove();
    this.isdatepicker = false;
  }

  addheats(datajs: any) {
    let array1 = [];
    for (let i = 0; i < datajs.markerPositions.length; i++) {
      const markerPosition = new Point(
        fromLonLat([
          parseFloat(datajs.markerPositions[i][1]),
          parseFloat(datajs.markerPositions[i][0]),
        ])
      );

      const markerFeature = new Feature({
        geometry: markerPosition,
      });

      array1.push(markerFeature);
    }
    console.log('array1::::::', array1);

    const vectorSource = new VectorSource({
      features: array1,
    });

    const markerLayer = new Heatmap({
      source: vectorSource,
      opacity: 0.5,
      zIndex: 0,
    });
    this.heats.push(markerLayer);
    console.log('markerLayer::::::::::::::', markerLayer);
    this.map.addLayer(markerLayer);
  }

  controlHeats(event: any) {
    if (event === false) {
      //alert('111111111')

      // console.log("removeeeeeheatsssss:::::",this.heats);
      // for (const layer of this.heats) {
      //   this.map.removeLayer(layer);
      //   this.map.render()
      // }

      // this.map.updateSize();
      this.addheats(this.datajson);
    } else {
      // //alert('2222222222')
      // console.log("addddddddheatsssss:::::",this.heats);
      // for (const layer of this.heats) {
      //   this.map.addLayer(layer);
      //   // this.map.updateSize();
      //   layer.refresh({force:true});
      // }
      //    this.heatmapLayer.visibility = true;

      this.map.updateSize();
      // this.map.removeLayer(this.clusters);
      // this.map.addLayer(this.clusters);
      console.log('removeeeeeheatsssss:::::', this.heats);
      for (const layer of this.heats) {
        this.map.removeLayer(layer);
        this.map.render();
      }

      this.map.updateSize();
    }
  }

  controlClusters(event: any) {
    if (event === false) {
      this.map.removeLayer(this.clusters);
    } else {
      this.map.addLayer(this.clusters);
    }
  }

  controlTooltip(event: any) {
    if (event === false) {
      const tooltipElements = this.el.nativeElement.querySelectorAll(
        'div[id="nameTooltip"]'
      );
      tooltipElements.forEach((element: HTMLElement) => {
        this.renderer.setStyle(element, 'display', 'none');
      });
    } else {
      console.log('nameshapeparameters:::::', this.nameshapeparameters);
      const tooltipElements = this.el.nativeElement.querySelectorAll(
        'div[id="nameTooltip"]'
      );
      tooltipElements.forEach((element: HTMLElement) => {
        this.renderer.setStyle(element, 'display', '');
      });
    }
  }
  controldateTooltip(event: any) {
    if (event === false) {
      $('#dateTooltip').show();
    } else {
      $('#dateTooltip').hide();
    }
  }

  controlArea(event: any) {
    if (event === false) {
      this.vectorSource.clear();
      this.darwLayer.getSource()?.clear();

      console.log(
        'circlessssssssssssssssssssssssssssssssssssssssss:::::::::::::::::',
        this.circles
      );
      for (const layer of this.circles) {
        this.map.removeLayer(layer);
      }
      for (const layer of this.polygons) {
        this.map.removeLayer(layer);
      }

      for (const layer of this.polylines) {
        this.map.removeLayer(layer);
      }
    } else {
      this.coordinatesArray.forEach((item) => {
        const type = item.Type;
        if (type === 'Circle') {
          const center = item.center;
          const radius = item.radius;
          const id = item.ID;
          var { lng, lat } = center;
          this.displayCircle(lng, lat, radius, id);
        } else if (type === 'Polygon') {
          this.displayPolygon(item);
        } else if (type === 'LineString') {
          this.displayPolyline(item, 'simulation');
        }
      });
      this.map.removeLayer(this.clusters);
      this.map.addLayer(this.clusters);
    }
  }

  filterData2() {
    
    this.uniqueNames = [];
    this.display = !this.display;
    // this.layerpopupcomp.openPopup();
    const markerPositions = this.datajson.markerPositions;
    this.uniqueNames = [
      ...new Set(markerPositions.map((item: any) => item[4])),
    ];
    console.log('uniqueNames::::::', this.uniqueNames);
    if (this.display === false) {
      this.clusters2 = [];
      this.clusterFeatures2 = [];
      this.uniqueNames = [];
    }
  }

  deviceClusters(eventData: {
    event: any;
    name: any;
    checkedCount: number;
    namearray: any[];
  }) {
    let { event, name, checkedCount, namearray } = eventData;
    this.map.removeLayer(this.clusters2);
    console.log('event:', event);
    console.log('name:', name);
    console.log('checkedCount:', checkedCount);
    console.log('namearray:', namearray);
    let checkednames = namearray;
    // if (checkedCount === 0) {
      // this.map.removeLayer(this.clusters2);
      this.clusters2 = [];
      // this.map.addLayer(this.clusters);
      this.clusterFeatures2 = [];
      this.source2=[];
      this.clusterSource2=[];
    // } else {
      // console.log('ifchecikeddddd:::::', event.target.checked);
      // console.log('nameeventttt:::::', event.target.id);
      // this.map.removeLayer(this.clusters2);
      // this.clusters2 = [];
      // this.map.removeLayer(this.clusters);
      this.clusterFeatures2 = [];
      let eventName: any = event.target.id;
      if (event.target.checked === false) {
        checkednames = checkednames.filter(
          (item) => item.layer_control !== eventName
        );

        console.log('arrayyyyyyyyyyyyy:', checkednames);
        this.map.removeLayer(this.clusters2);
      }
// else
//     {  
      checkednames.forEach((name: any) => {
        console.log('eventttttttttttttt:', event);
        console.log('device clusters name:::::::::::', name.layer_control);
        name = name.layer_control;
        let x = this.findObjectsByElement4(this.datajson, name);
        console.log('findObjectsByElement4:::::::::::', x);
        let data: any = {
          markerPositions: x,
        };
        console.log('fianl obejctssss:::::::::::', data);

        // this.clusterFeatures2=[];
        for (let i = 0; i < data.markerPositions.length; i++) {
          this.marker2 = new Feature(
            new Point([
              parseFloat(data.markerPositions[i][1]),
              parseFloat(data.markerPositions[i][0]),
            ])
          );
          this.marker.set('name', 'marker');
          this.marker2.set('info', data.markerPositions[i]);
          this.clusterFeatures2.push(this.marker2);
        }
      });
    // }
      this.source2 = new VectorSource({
        features: this.clusterFeatures2,
      });
      this.clusterSource2 = new Cluster({
        source: this.source2,
      });
      const styleCache: { [key: number]: Style } = {};
      this.clusters2 = new VectorLayer({
        source: this.clusterSource2,
        style: (feature) => {
          const size = feature.get('features').length;
          let style = styleCache[size];
          if (!style) {
            if (size < 20) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#2EFF2E40',
                  }),
                  fill: new Fill({
                    color: '#2EFF2E90',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            } else if (size >= 20 && size < 100) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#FFFF2E40',
                  }),
                  fill: new Fill({
                    color: '#FFFF2E90',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            } else if (size >= 100) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#FF660040',
                  }),
                  fill: new Fill({
                    color: '#FF660090',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            }
          }
          return style;
        },
      });
      this.clusters2Array.push(this.clusters2);
      console.log('clustersss22222222222222222:', this.clusters2);
      this.map.addLayer(this.clusters2);
    // }
  }

  findObjectsByElement4(datajson: any, elementValue: string): any[] {
    return datajson.markerPositions.filter(
      (item: any) => item[4] === elementValue
    );
  }

  createOverlayNamingShape(coord: any) {
    var popup = this.elem_id('popup11111');
    var popup_closer = this.elem_id('popup-closer')!;
    this.olpopup = new ol.Overlay({
      element: popup!,
    });
    this.map.addOverlay(this.olpopup);
    popup_closer.onclick = () => {
      this.olpopup.setPosition(undefined);
      popup_closer.blur();
      this.nameshapevalue = false;
      return false;
    };

    $('#popup11111').show();
    this.olpopup.setPosition(coord);
    this.lastOverlay = this.olpopup;
  }

  showMeasurepopup() {
    console.log("22222222222222222222")
    this.measurepopup = !this.measurepopup;
  }

  // cancelModify() {
  //   alert("cancel")
  //   this.vectorSource.getFeatures().forEach((feature:any) => {
  //   alert("cancelModify")
  //   console.log("feature:",feature);

  //     const modifyGeometry = feature.get('modifyGeometry');
  //     console.log("modifyGeometry:",modifyGeometry);
  //     if (modifyGeometry) {
  //       feature.setGeometry(modifyGeometry.geometry);
  //       console.log("featureeeeeeeeeeee:",feature.setGeometry(modifyGeometry.geometry));
  //       console.log("modifyGeometry.geometry:",modifyGeometry.geometry);

  //       feature.unset('modifyGeometry', true);
  //     }
  //   });
  // }
  // start(){
  //   alert("start");

  //   this.modify.on('modifystart',   (event:any)=> {
  //     alert("startmodify");

  //     console.log('event.features', event.features);
  //     event.features.forEach(  (feature:any)=> {
  //         // geometry -> modifyGeometry
  //         feature.set(
  //             'modifyGeometry',
  //             {geometry: feature.getGeometry().clone()},
  //             true
  //         );
  //     });
  // });

  // }

  // end(){
  //   alert("end");
  //   this.modify.on('modifyend',  (event:any) =>{
  //   alert("end modify");

  //     event.features.forEach( (feature:any) =>{
  //         const modifyGeometry = feature.get('modifyGeometry');
  //         // modifyGeometry -> geometry
  //         if (modifyGeometry) {
  //             feature.setGeometry(modifyGeometry.geometry);
  //             feature.unset('modifyGeometry', true);
  //         }
  //     });
  // });
  // }
 
  // changeMap(event: Event) {
  //   this.url = '';
  //   const selectElement = event.target as HTMLSelectElement;

  //   // Get the selected option element
  //   const selectedOption = selectElement.selectedOptions[0];

  //   // Get the id attribute of the selected option
  //   const selectedOptionId = selectedOption.id;
  //   // Now you have the id of the selected option, you can use it as needed
  //   console.log('Selected Option ID: ', selectedOptionId);

  //   if (selectedOptionId === 'OpenStreet') {
  //     // this.url='https://10.1.7.24:443/openstreetmap/{z}/{x}/{y}.png';
  //     this.url = 'https://' + this.ip + '/openstreetmap/{z}/{x}/{y}.png';
  //     console.log('this.url11111111111:', this.url);
  //     const newSource = new XYZ({
  //       url: this.url,
  //       minZoom: 0,
  //       maxZoom: 19,
  //     });

  //     // Set the new source for the map layer
  //     this.mapLayer.setSource(newSource);
  //   } else {
  //     this.url = 'https://' + this.ip + '/openstreetmaplebanon/{z}/{x}/{y}.png';
  //     console.log('this.url22222222222:', this.url);

  //     const newSource = new XYZ({
  //       url: this.url,
  //       minZoom: 0,
  //       maxZoom: 19,
  //     });

  //     // Set the new source for the map layer
  //     this.mapLayer.setSource(newSource);
  //   }
  // }


  changeMap(event: Event) {
    this.url = '';
    const selectElement = event.target as HTMLSelectElement;

    // Get the selected option element
    const selectedOption = selectElement.selectedOptions[0];

    // Get the id attribute of the selected option
    const selectedOptionId = selectedOption.id;
    // Now you have the id of the selected option, you can use it as needed
    console.log('Selected Option ID: ', selectedOptionId);
   let newurl:any[]= this.maptypes.filter((id:any)=>{
    return id[2]===selectedOptionId;
    });
    console.log("00000000000----------",newurl);
    console.log("newurl[3]----------",newurl[0][3]);
    console.log("newurl[1]----------",newurl[0][1]);
    this.url = 'https://' +newurl[0][3] + '/'+newurl[0][1]+'/{z}/{x}/{y}.png';
    console.log("newww url ------------",this.url);
        const newSource = new XYZ({
        url: this.url,
        minZoom: newurl[0][4],
        maxZoom: newurl[0][5],
      });

      this.mapLayer.setSource(newSource);
  }

 


  arrayToString(arr: any[]): string {
    return arr.join(",");
  }


  RunDeviceHistory() {
    // alert(' in RunDeviceHistory angular:');
    console.log('RunDeviceHistory-----------');

    let device: any = this.dataservice1.getDHselectedDevice();

    console.log('device-----------', device);
    let devices:any[]=[];
    device.forEach((element:any)=>{
      devices.push(element.key)
    });
    devices =[...new Set(devices)];

let deviceh=this.arrayToString(devices);

console.log("RunDeviceHistory-----------",deviceh);
    (window.parent.parent.parent[7] as any).RunDeviceHistory(deviceh);
  }

  drawarc(
    lat: any,
    lng: any,
    radius: any,
    bearing1: any,
    bearing2: any,
    color: any,
    object: any,
    sinfo2: any
  ) {
    // Create a sector as a polygon
    // Create a sector using turf.js
    var center = [lng, lat];

    let azimuth = bearing1;

    if (Number.isNaN(bearing1)) {
    } else {
      radius = (100 + 7) / 1000;
      bearing1 = bearing1 - 25;
      bearing2 = bearing2 + 25;
      var options: any = {
        steps: 10,
        units: 'degrees',
        properties: { foo: 'bar' },
      };
      var sector = turf.sector(center, radius, bearing1, bearing2);

      if (object.length > 0) {
        sector.properties = {
          type: 'sector',
          Azimuth: azimuth,
          lng: lng,
          lat: lat,
          Technology: object[0][10],
          SINFO: object,
          SINFO2: '',
        };
      } else {
        if (sinfo2.length > 0) {
          sector.properties = {
            type: 'sector',
            Azimuth: azimuth,
            lng: lng,
            lat: lat,
            Technology: '',
            SINFO: '',
            SINFO2: sinfo2,
          };
        } else {
          sector.properties = {
            type: 'sector',
            Azimuth: '',
            lng: '',
            lat: '',
            Technology: '',
            SINFO: '',
            SINFO2: '',
          };
        }
      }

      const sectorFeatureCollection = turf.featureCollection([sector]);
      const sectorFeature = new GeoJSON().readFeature(sector);

      this.Fixedsectorarray.push(sectorFeature);
      this.FixedsectorarrayLoop.push(sectorFeature);
   
    }
  }

  drawarc2() {
    // Create a sector as a polygon
    // Create a sector using turf.js
    var center = [35.33, 33];
    let radius = (100 + 7) / 1000;
    let bearing1 = 180 + 90 - 25;
    let bearing2 = 180 + 90 + 25;
    var options: any = {
      steps: 10,
      units: 'degrees',
      properties: { foo: 'bar' },
    };
    var sector = turf.sector(center, radius, bearing1, bearing2);

    // Create a vector source and add the sector to it
    sector.properties = {
      Azimuth: '11111111',
      lng: '1111111111111',
      lat: '111111111',
      Technology: '1111111111',
    };

    const sectorFeatureCollection = turf.featureCollection([sector]);

    // Create a vector source and add the sector to it
    var vectorSource = new VectorSource({
      features: new GeoJSON().readFeatures(sectorFeatureCollection),
    });

    const vectorLayer = new VectorLayer({
      source: vectorSource,
    });

    this.map.addLayer(vectorLayer);
    this.view = new ol.View({
      center: center,
      zoom: 17,
    });

    this.map.setView(this.view);
    const select = new Select({
      condition: (event) => {
        return event.originalEvent.button === 2; // Right-click condition
      },
    });

    // Add the select interaction to the map
    this.map.addInteraction(select);
  }

  createArc(
    center: any,
    radius: number,
    startAngle: any,
    endAngle: any
  ): Geometry {
    const numSegments = 100;

    const coordinates = [];
    for (let i = 0; i <= numSegments; i++) {
      const angle = startAngle + (i / numSegments) * (endAngle - startAngle);
      const x = center[0] + radius * Math.cos(angle);
      const y = center[1] + radius * Math.sin(angle);
      coordinates.push([x, y]);
    }

    return new LineString(coordinates);
  }

  getSectorCoordinates(
    center: any,
    radius: any,
    startAngle: any,
    stopAngle: any
  ): Geometry {
    const angleStep = 5;
    const coordinates = [];

    for (let angle = startAngle; angle <= stopAngle; angle += angleStep) {
      const radians = (angle - 90) * (Math.PI / 180);
      const lat = center[0] + (radius * Math.cos(radians)) / 111.32;
      const lon =
        center[1] +
        (radius * Math.sin(radians)) /
          (111.32 * Math.cos(center[0] * (Math.PI / 180)));

      coordinates.push([lat, lon]);
    }

    coordinates.push(center);

    return new LineString(coordinates);
  }
  closecotravelerpopup() {
    // alert('close popup')
    this.cotraveler = false;
    this.olpopupOverlay.setPosition(undefined);
  }
  // testarc(){
  //   // alert("testarc")
  // console.log('2222222222222222222222222222222:',this.CdrData.length);
  // let src:any;

  //   for(let i=0;i<this.CdrData.length;i++){

  //    let SectorMeter=100;
  //     this.displayBTS(this.CdrData[i].BTS,this.CdrData[i]);
  //     console.log('this.CdrData>>>',this.CdrData[i]);
  //     for(let j=0;j<this.CdrData[i].SECTORS.length;j++){
  //     console.log('numberrrrrrrrrrrrrrrrrr>>>',Number(this.CdrData[i].SECTORS[j]));

  //     this.drawarc(Number(this.CdrData[i].BTS.LATITUDE),Number(this.CdrData[i].BTS.LONGITUDE),SectorMeter,Number(this.CdrData[i].SECTORS[j]),Number(this.CdrData[i].SECTORS[j]),'blue','');
  //    // this.drawarc(Number(this.CdrData[i].BTS.LATITUDE),Number(this.CdrData[i].BTS.LONGITUDE),SectorMeter,200,200,'blue','');

  //     }
  //     }
  //     this.sourcebts = new VectorSource({
  //       features: this.fixedBtsArray,
  //     });
  //     console.log("source>>>",  this.source);

  //     this.clusterSourcebts = new Cluster({
  //       minDistance: 100,
  //       source: this.sourcebts,
  //     });
  //     console.log('clusterSource>>>>>>>>',this.clusterSource)
  //     const styleCache: { [key: number]: Style } = {};

  //     this.fixedbtsGroup = new VectorLayer({
  //       source: this.clusterSourcebts,
  //       zIndex: 9999,
  //       style: function (bts) {
  //         console.log("cluster:::::",bts);
  //         const size = bts.get('features').length;
  //         console.log('size::::',size);
  //         let style = styleCache[size];
  //         if (!style) {

  //             src ='cybercrowd/angular-offline/assets/icons/BTS.png';

  //           styleCache[size] = style;
  //         }

  //         return new Style({
  //           image: new Icon({

  //             src: src,
  //             scale: 0.8,
  //             // size:[32,32]
  //           }),
  //           text: new Text({
  //             text: size.toString(),
  //             fill: new Fill({ color: '#fff' }),
  //           }),
  //         });

  //       },
  //     });

  //     this.fixedbtsGroup.set('A_Type', 'bts');
  //     this.map.addLayer(this.fixedbtsGroup);

  //     const select = new Select({
  //       condition: (event) => {
  //         return event.originalEvent.button === 2; // Right-click condition
  //       },
  //     });

  //     // Add the select interaction to the map
  //     this.map.addInteraction(select);

  //   //   select.on('select', (event) => {

  //   //     $('#btspopup').css('display', '');
  //   // this.bts=true;
  //   //     $('#datepicker').css('display', 'none');

  //   //     this.deleteMode=false;
  //   //     this.selectedFeatures = event.target.getFeatures();

  //   //     this.selectedFeatures.forEach((feature: { getProperties: () => any }) => {
  //   //       console.log(`feature >>>>>>>>>>>>>>>>`, feature);
  //   //       const properties = feature.getProperties();
  //   //       console.log(`properties >>>>>>>>>>>>>>>>`, properties);
  //   //       console.log(`flatCoordinates>>>>>>>>>>>>>>>>`, properties.geometry.flatCoordinates);
  //   //       if (properties.id) {

  //   //       } else if(properties.features) {

  //   //         console.log(`properties.id >>>>>>>>>>>>>>>>`, properties.features.length);
  //   //         console.log(`1111111111111111111111111 >>>>>>>>>>>>>>>>`, properties.features);
  //   //         for (let i = 0; i < properties.features.length; i++) {
  //   //           console.log("22222222222222222222222222",properties.features[i]['values_']['info']);

  //   //           this.rowDatabts.push({
  //   //            Type: `${ properties.features[i]['values_']['info'].BTS.TYPE}`,
  //   //             Location: `${properties.features[i]['values_']['info'].INFO[0][5]}`,
  //   //             BTSName: `${properties.features[i]['values_']['info'].INFO[0][9]}`,
  //   //             Sector: `${properties.features[i]['values_']['info'].INFO[0][1]}`,
  //   //             Frequency: `${properties.features[i]['values_']['info'].INFO[0][6]}`,
  //   //             Lng: `${properties.features[i]['values_']['info'].INFO[0][3]}`,
  //   //             Lat: `${properties.features[i]['values_']['info'].INFO[0][2]}`,
  //   //             Azimuth: `${properties.features[i]['values_']['info'].INFO[0][1]}`,
  //   //           });
  //   //         }
  //   //         console.log("444444444444444444")
  //   //         this.columnDefsBts = [
  //   //           {
  //   //             headerName: 'Type',
  //   //             field: 'Type',
  //   //             sortable: true,
  //   //             filter: 'agSetColumnFilter',
  //   //             width: 100,
  //   //             wrapText: true,
  //   //             autoHeight: true,
  //   //             cellStyle: { flex: '1' },

  //   //           },
  //   //           {
  //   //             headerName: 'Location',
  //   //             field: 'Location',
  //   //             sortable: true,
  //   //             width: 100,
  //   //             filter: 'agSetColumnFilter',
  //   //             autoSizeColumns: true,
  //   //             cellStyle: { 'text-align': 'right', color: 'blue' },
  //   //           },
  //   //           {
  //   //             headerName: 'BTSName',
  //   //             field: 'BTSName',
  //   //             width: 100,
  //   //             sortable: true,
  //   //             filter: 'agSetColumnFilter',
  //   //             autoSizeColumns: true,
  //   //             cellStyle: { 'text-align': 'right' },
  //   //           },
  //   //           {
  //   //             headerName: 'Sector',
  //   //             field: 'Sector',
  //   //             sortable: false,
  //   //             filter: false,
  //   //             autoSizeColumns: true,
  //   //             width: 100,
  //   //             cellStyle: { 'text-align': '-webkit-center', flex: '1' },
  //   //           },
  //   //           {
  //   //             headerName: 'Frequency',
  //   //             field: 'Frequency',
  //   //             width: 100,
  //   //             sortable: true,
  //   //             filter: 'agSetColumnFilter',
  //   //             autoSizeColumns: true,
  //   //             cellStyle: { 'text-align': 'right' },
  //   //           },
  //   //           {
  //   //             headerName: 'Lng',
  //   //             field: 'Lng',
  //   //             width: 100,
  //   //             sortable: true,
  //   //             filter: 'agSetColumnFilter',
  //   //             autoSizeColumns: true,
  //   //             cellStyle: { 'text-align': 'right' },
  //   //           },
  //   //           {
  //   //             headerName: 'Lat',
  //   //             field: 'Lat',
  //   //             width: 100,
  //   //             sortable: true,
  //   //             filter: 'agSetColumnFilter',
  //   //             autoSizeColumns: true,
  //   //             cellStyle: { 'text-align': 'right' },
  //   //           },
  //   //           {
  //   //             headerName: 'Azimuth',
  //   //             field: 'Azimuth',
  //   //             width: 100,
  //   //             sortable: true,
  //   //             filter: 'agSetColumnFilter',
  //   //             autoSizeColumns: true,
  //   //             cellStyle: { 'text-align': 'right' },
  //   //           },

  //   //         ];

  //   //         //// open popup
  //   //         console.log(`properties >>>>>>>>>>>>>>>>`, properties.features);
  //   //         var popup:any = this.elem_id('btspopup');
  //   //         var popup_closer = this.elem_id('popup-closerbts')!;

  //   //         var coord0=properties.geometry.flatCoordinates[1];
  //   //         var coord1=properties.geometry.flatCoordinates[0];
  //   //          this.olpopupOverlay = new ol.Overlay({
  //   //           element: popup,
  //   //           autoPan: false,
  //   //         });

  //   //         this.olpopupOverlay.setPosition([coord1,coord0]);
  //   //         this.map.addOverlay(this.olpopupOverlay);

  //   //     console.log("eventtcotraveler::::",event.selected[0]);
  //   //         // this.openPopupp(event, olpopup);
  //   //         $('#popupGrid').css('display', 'none');
  //   //         $('#popup2').css('display', 'none');
  //   //         $('#cotravelerpopup').css('display', 'none');
  //   //         this.deactivateDrawInteraction();

  //   //       }
  //   //     });
  //   //   });
  // }

  displayBTS(object: any, info: any) {
    let bts: any = new Feature(
      new Point([Number(object.LONGITUDE), Number(object.LATITUDE)])
    );
    bts.set('info', info);
    bts.set('type', 'bts');
    this.fixedBtsArray.push(bts);
    this.fixedBtsArrayLoop.push(bts);
  }
displayBtsOnMap(){
  let src: any;
  this.sourcebtsLoop = new VectorSource({
    features: this.fixedBtsArrayLoop,
  });
  console.log('source>>>', this.source);

  this.clusterSourcebtsLoop = new Cluster({
    minDistance: 100,
    source: this.sourcebtsLoop,
  });
   const styleCache: { [key: number]: Style } = {};

  this.fixedbtsGroupLoop = new VectorLayer({
    source: this.clusterSourcebtsLoop,
    zIndex: 9999,
    style: function (bts) {
      // console.log("cluster:::::",bts);
      const size = bts.get('features').length;
      // console.log('size::::',size);
      let style = styleCache[size];
      if (!style) {
        src = '/cybercrowd/angular-offline/assets/icons/BTS.png';

        styleCache[size] = style;
      }

      return new Style({
        image: new Icon({
          src: src,
          scale: 0.8,
          // size:[32,32]
        }),
        text: new Text({
          text: size.toString(),
          fill: new Fill({ color: '#fff' }),
        }),
      });
    },
  });

  this.fixedbtsGroupLoop.set('A_Type', 'tcd');
  this.fixedbtsGroupArrayLoop.push(this.fixedbtsGroupLoop);
  this.magnifiedMap.addLayer(this.fixedbtsGroupLoop);

}
  closebtspopup() {
    this.rowDatabts = [];
    this.bts = false;
    this.olpopupOverlay.setPosition(undefined);
    $('#btspopup').css('display', 'none');
  }

  scandevices() {
    if (this.clusters) {
      this.map.removeLayer(this.clusters);
      this.clusters = [];
    }

    let unique: any = {};
    let distinctData = this.datajson.markerPositions.filter(
      (obj: any) => !unique[obj[2]] && (unique[obj[2]] = true)
    );

    console.log('1111---------', distinctData);

    for (let i = 0; i < distinctData.length; i++) {
      let point = new Point([
        parseFloat(distinctData[i][1]),
        parseFloat(distinctData[i][0]),
      ]);
      this.markerDev = new Feature({
        geometry: point,
        name: 'Marker',
      });
      this.markerDev.set('type', 'markerdev');
      console.log('nameee--------------', distinctData[i][2]);
      let infodata = this.datajson.markerPositions.filter(
        (item: any) => item[2] === distinctData[i][2]
      );
      console.log('infodata--------------', infodata);
      this.markerDev.set('info', infodata);
      this.clusterFeaturesdev.push(this.markerDev);
    }

    this.sourcedev = new VectorSource({
      features: this.clusterFeaturesdev,
    });
    this.clustersourcedev = new Cluster({
      source: this.sourcedev,
    });

    const styleCache: { [key: number]: Style } = {};

    this.clustersdev = new VectorLayer({
      source: this.clustersourcedev,
      style: (feature) => {
        const size = feature.get('features').length;
        let style = styleCache[size];
        if (!style) {
          if (size < 20) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#2EFF2E40',
                }),
                fill: new Fill({
                  color: '#2EFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 20 && size < 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FFFF2E40',
                }),
                fill: new Fill({
                  color: '#FFFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FF660040',
                }),
                fill: new Fill({
                  color: '#FF660090',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
          }
          styleCache[size] = style;
        }
        return style;
      },
    });
    this.clustersdev.set('type', 'markerdev');
    this.map.addLayer(this.clustersdev);
  }

  filterArray() {
    const uniqueNames: any = {};
    let filteredArray: any[] = [];
    let inputArray: any;
    filteredArray = inputArray.filter((item: any) => {
      const { name, class: itemClass } = item;

      if (!uniqueNames[name]) {
        uniqueNames[name] = true;
        return true;
      }

      return false;
    });
  }

    closeTCDPopup() {
    $('#TCDContent').css('display', 'none');
    // // $('#popup2').css('display', 'none');
    this.TcdRowData = [];
    // alert('close')
  }

  
  displayarcLoop() {
    // Create a vector source and add the sector to it
    var vectorSourceLoop = new VectorSource({
      features: this.FixedsectorarrayLoop,
    });

    this.vectorLayerSectorLoop = new VectorLayer({
      source: vectorSourceLoop,
    });

 
    this.magnifiedMap.addLayer(this.vectorLayerSectorLoop);
    this.FixedsectorarrayLoopLayer.push(this.vectorLayerSectorLoop);
 
  }
  displayarc() {
    // Create a vector source and add the sector to it
    var vectorSource = new VectorSource({
      features: this.Fixedsectorarray,
    });

    this.vectorLayerSector = new VectorLayer({
      source: vectorSource,
    });

  
    this.map.addLayer(this.vectorLayerSector);

this.displayarcLoop();
 
  }

  DisplayCdr() {
  
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_isBtsall
    ) {
      console.log(
        'A_isBtsall',
        (window.parent.parent.parent[7] as any).A_isBtsall
      );
      this.isBtsall = (window.parent.parent.parent[7] as any).A_isBtsall;
      console.log('isBtsall>>', this.isBtsall);
    }
    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_isBTS
    ) {
      console.log('A_isBTS', (window.parent.parent.parent[7] as any).A_isBTS);
      this.isBTS = (window.parent.parent.parent[7] as any).A_isBTS;
      console.log('isBTS>>', this.isBTS);
    }

    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_simulationid
    ) {
      this.simulationid = (
        window.parent.parent.parent[7] as any
      ).A_simulationid;
    }
    if (this.isBTS == true) {
      if (this.fixedMarkersGroup) {
        this.map.removeLayer(this.fixedElementMarker);
      }
      if (this.fixedElementMarker) {
        this.map.removeLayer(this.fixedElementMarker);
      }
      if (this.fixedMarkersArray.length > 0) {
        this.map.removeLayer(this.fixedMarkersGroup);
        this.fixedMarkersArray = [];
      }
      console.log('isFixedElements>>>>>>>>>>>>>>>>>>');

      (window.parent.parent.parent[7] as any).ResetBooleanVar();

      console.log('ObjectID beforeeee>>>>>', this.ObjectID);
      let numArrbefore;
      if (typeof this.ObjectID != 'undefined' && !isNaN(this.ObjectID)) {
        numArrbefore = this.ObjectID.join().split(',').map(Number);
      }

      this.ObjectID = (window.parent.parent.parent[7] as any).A_ObjectID;
      let numArr = this.ObjectID.join().split(',').map(Number);
      if (typeof numArrbefore != 'undefined' && !isNaN(numArrbefore)) {
        for (let i = 0; i < numArr.length; i++) {
          numArrbefore.push(numArr[i]);
        }
        numArr = this.getUniqueNumbers(numArrbefore);
      }
      let numArrfinall = numArr.map((num: any) => num.toString());
      this.ObjectID = numArrfinall;

      if ((window.parent as any) && (window.parent as any).AV2_userCode) {
        this.usercode = (window.parent as any).AV2_userCode;
      }

      console.log('ObjectID>>>>>', this.ObjectID);

      if ((window.parent.parent.parent[7] as any) && (window.parent.parent.parent[7] as any).A_sqlcond) {
        console.log("A_sqlcond", (window.parent.parent.parent[7] as any).A_sqlcond)
        this.isSqlCond = (window.parent.parent.parent[7] as any).A_sqlcond;
        console.log("A_sqlcond>>", this.isSqlCond)
  
      }


   
      if (typeof (window.parent.parent.parent[7] as any).A_sqlcond == "undefined") {
        this.isSqlCond = "";
      }
      
      this.BTSObject= {
        sqlcond:this.isSqlCond,
        ids:this.ObjectID
      }
      this.datacrowdService
        .getfixedelementsObject2BTS(JSON.stringify(this.BTSObject), this.usercode)
        .then((res) => {
          console.log('getfixedelementsObject2BTS>>>>', res);

          this.CdrData = res;

          console.log('x', this.CdrData);
          console.log('x000', this.CdrData[0]);

          this.CdrRowData = [];
          let src: any;

          for (let i = 0; i < this.CdrData[0].length; i++) {
            this.displayBTS(this.CdrData[0][i].BTS, this.CdrData[0][i].INFO);

            for (let j = 0; j < this.CdrData[0][i].SECTORS.length; j++) {
              this.drawarc(
                Number(this.CdrData[0][i].BTS.LATITUDE),
                Number(this.CdrData[0][i].BTS.LONGITUDE),
                this.SectorMeter,
                Number(this.CdrData[0][i].SECTORS[j]),
                Number(this.CdrData[0][i].SECTORS[j]),
                this.SectorColor,
                '',
                this.CdrData[0][i].INFO
              );
            }
          }
          this.sourcebts = new VectorSource({
            features: this.fixedBtsArray,
          });

          this.clusterSourcebts = new Cluster({
            minDistance: 100,
            source: this.sourcebts,
          });

          const styleCache: { [key: number]: Style } = {};

          this.fixedbtsGroup = new VectorLayer({
            source: this.clusterSourcebts,
            zIndex: 9999,
            style: function (bts) {
              const size = bts.get('features').length;
              let style = styleCache[size];
              if (!style) {
                src = '/cybercrowd/angular-offline/assets/icons/BTS.png';

                styleCache[size] = style;
              }

              return new Style({
                image: new Icon({
                  src: src,
                  scale: 0.8,
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({ color: '#fff' }),
                }),
              });
            },
          });

          this.fixedbtsGroup.set('A_Type', 'tcd');
          this.fixedbtsGroupArray.push(this.fixedbtsGroup);
          this.map.addLayer(this.fixedbtsGroup);
          this.displayBtsOnMap();
          this.displayarc();
          this.isTcd = false;
        });

      this.isAOi = false;
      this.isSimul = false;
      this.isFixedElements = false;
      this.isFixedElementsall = false;
      this.isBTS = false;
      this.isBtsall = false;
    }
  }

  opentimelineScreen() {
   
    (window.parent as any).hideSimulation();
    

       
    this.displayedColumns = ['Time', 'event', 'Lng', 'lat'];
    // this.openTable = true;
if($('#tabletest').css('display') === 'block'){

  $('#tabletest').css('display','none');

}else{
  $('#tabletest').css('display','block');

}
    // if ((this.openTable = true)) {
    //   this.openTable = true;
    // } else {
    //   this.openTable = false;
    // }
  }

  changeview(eventData: any) {
    console.log('eventData', eventData);

    this.view = new ol.View({
      center: [eventData.row.Lng, eventData.row.lat],
      zoom: 17,
    });

    this.map.setView(this.view);
  }

  displayPolylinetest() {
    let x:any=[
      [
          34.4085117,
          35.8184616
      ],
      [
          34.4204491,
          35.8308841
      ],
      [
          34.4204491,
          35.8308841
      ],
      [
          34.420858,
          35.8280376
      ],
      [
          34.420858,
          35.8280376
      ],
      [
          34.4245409,
          35.8270469
      ],
      [
          34.4245409,
          35.8270469
      ],
      [
          34.4357441,
          35.831
      ],
      [
          34.4357441,
          35.831
      ],
      [
          34.4346381,
          35.8323361
      ],
      [
          34.4346381,
          35.8323361
      ],
      [
          34.443753,
          35.8225922
      ],
      [
          34.443753,
          35.8225922
      ],
      [
          34.4414816,
          35.8196738
      ],
      [
          34.4414816,
          35.8196738
      ],
      [
          34.4323681,
          35.8207715
      ],
      [
          34.4323681,
          35.8207715
      ],
      [
          34.4231876,
          35.8233785
      ],
      [
          34.4231876,
          35.8233785
      ],
      [
          34.4153362,
          35.8221897
      ],
      [
          34.4153362,
          35.8221897
      ],
      [
          34.4090163,
          35.8191227
      ],
      [
          34.4090163,
          35.8191227
      ],
      [
          34.4084307,
          35.8185404
      ],
      [
          34.4084307,
          35.8185404
      ],
      [
          34.4091594,
          35.8179927
      ],
      [
          34.4091594,
          35.8179927
      ],
      [
          34.4093697,
          35.8186213
      ],
      [
          34.4093697,
          35.8186213
      ],
      [
          34.4095632,
          35.8178577
      ],
      [
          34.4095632,
          35.8178577
      ],
      [
          34.4086495,
          35.8183912
      ],
      [
          34.4086495,
          35.8183912
      ],
      [
          34.420939,
          35.8310906
      ],
      [
          34.420939,
          35.8310906
      ],
      [
          34.4208417,
          35.8257852
      ],
      [
          34.4208417,
          35.8257852
      ],
      [
          34.4230102,
          35.8238277
      ],
      [
          34.4230102,
          35.8238277
      ],
      [
          34.4352673,
          35.8202117
      ],
      [
          34.4352673,
          35.8202117
      ],
      [
          34.4439361,
          35.8225362
      ],
      [
          34.4439361,
          35.8225362
      ],
      [
          34.4501156,
          35.8342366
      ],
      [
          34.4501156,
          35.8342366
      ],
      [
          34.4547608,
          35.8585475
      ],
      [
          34.4547608,
          35.8585475
      ],
      [
          34.4516249,
          35.8614708
      ],
      [
          34.4516249,
          35.8614708
      ],
      [
          34.4504192,
          35.8603381
      ],
      [
          34.4504192,
          35.8603381
      ],
      [
          34.4545835,
          35.8691024
      ],
      [
          34.4545835,
          35.8691024
      ],
      [
          34.458914,
          35.883867
      ],
      [
          34.458914,
          35.883867
      ],
      [
          34.4629376,
          35.8913061
      ],
      [
          34.4629376,
          35.8913061
      ],
      [
          34.4637315,
          35.9063679
      ],
      [
          34.4637315,
          35.9063679
      ],
      [
          34.4913887,
          35.9551604
      ],
      [
          34.4913887,
          35.9551604
      ],
      [
          34.5065183,
          35.9656054
      ],
      [
          34.5409034,
          36.1890166
      ],
      [
          34.5403982,
          36.1894456
      ],
      [
          34.5403982,
          36.1894456
      ],
      [
          34.541024,
          36.1891756
      ],
      [
          34.541024,
          36.1891756
      ],
      [
          34.5410533,
          36.1899677
      ],
      [
          34.5410533,
          36.1899677
      ],
      [
          34.5410352,
          36.1892673
      ],
      [
          34.4085129,
          35.8184025
      ],
      [
          34.40914,
          35.8175404
      ],
      [
          34.40914,
          35.8175404
      ],
      [
          34.4091619,
          35.8185794
      ],
      [
          34.4091619,
          35.8185794
      ],
      [
          34.4091781,
          35.8174972
      ],
      [
          34.4091781,
          35.8174972
      ],
      [
          34.4085996,
          35.8183996
      ],
      [
          34.4085154,
          35.8184069
      ],
      [
          34.4085154,
          35.8184069
      ],
      [
          34.4086458,
          35.8183915
      ],
      [
          34.4072264,
          35.8194916
      ],
      [
          34.4072264,
          35.8194916
      ],
      [
          34.442156,
          35.8272058
      ],
      [
          34.442156,
          35.8272058
      ],
      [
          34.4397479,
          35.830455
      ],
      [
          34.4397479,
          35.830455
      ],
      [
          34.4365072,
          35.8314599
      ],
      [
          34.4365072,
          35.8314599
      ],
      [
          34.435692,
          35.8309189
      ],
      [
          34.435692,
          35.8309189
      ],
      [
          34.4020062,
          35.8387689
      ],
      [
          34.4020062,
          35.8387689
      ],
      [
          34.4184884,
          35.8313725
      ],
      [
          34.4184884,
          35.8313725
      ],
      [
          34.414061,
          35.8239946
      ],
      [
          34.414061,
          35.8239946
      ],
      [
          34.4086008,
          35.8184664
      ],
      [
          34.4086008,
          35.8184664
      ],
      [
          34.4092958,
          35.8173968
      ],
      [
          34.4092958,
          35.8173968
      ],
      [
          34.408665,
          35.8184581
      ],
      [
          34.4402948,
          35.8297066
      ],
      [
          34.4356552,
          35.8312445
      ],
      [
          34.4356552,
          35.8312445
      ],
      [
          34.438195,
          35.818994
      ],
      [
          34.438195,
          35.818994
      ],
      [
          34.4164267,
          35.8230606
      ],
      [
          34.4164267,
          35.8230606
      ],
      [
          34.3988144,
          35.8095078
      ],
      [
          34.3988144,
          35.8095078
      ],
      [
          34.3673726,
          35.7537024
      ],
      [
          34.3673726,
          35.7537024
      ],
      [
          34.2782216,
          35.6929582
      ],
      [
          34.2782216,
          35.6929582
      ],
      [
          34.2103666,
          35.6517605
      ],
      [
          34.2103666,
          35.6517605
      ],
      [
          34.1405867,
          35.6388547
      ],
      [
          34.1405867,
          35.6388547
      ],
      [
          34.0070014,
          35.6491578
      ],
      [
          34.0070014,
          35.6491578
      ],
      [
          33.9918597,
          35.6433006
      ],
      [
          33.9918597,
          35.6433006
      ],
      [
          33.9859066,
          35.6400205
      ],
      [
          33.9859066,
          35.6400205
      ],
      [
          33.9840561,
          35.6383811
      ],
      [
          33.9840561,
          35.6383811
      ],
      [
          33.9781829,
          35.626657
      ],
      [
          33.9781829,
          35.626657
      ],
      [
          33.9786708,
          35.6192495
      ],
      [
          33.9786708,
          35.6192495
      ],
      [
          33.9173545,
          35.5839095
      ],
      [
          33.9173545,
          35.5839095
      ],
      [
          33.8988215,
          35.5682256
      ],
      [
          33.8988215,
          35.5682256
      ],
      [
          33.8956169,
          35.5429811
      ],
      [
          33.8956169,
          35.5429811
      ],
      [
          33.8976484,
          35.4958922
      ],
      [
          33.8976484,
          35.4958922
      ],
      [
          33.8870611,
          35.4938952
      ],
      [
          33.8870611,
          35.4938952
      ],
      [
          33.8806666,
          35.489621
      ],
      [
          33.8806666,
          35.489621
      ],
      [
          33.893088,
          35.4975644
      ],
      [
          33.893088,
          35.4975644
      ],
      [
          33.8945555,
          35.4949575
      ],
      [
          33.8945555,
          35.4949575
      ],
      [
          33.9144756,
          35.5825864
      ],
      [
          33.9144756,
          35.5825864
      ],
      [
          33.9326969,
          35.5891901
      ],
      [
          33.9326969,
          35.5891901
      ],
      [
          33.9472145,
          35.5927684
      ],
      [
          33.9472145,
          35.5927684
      ],
      [
          33.9655616,
          35.6066886
      ],
      [
          33.9655616,
          35.6066886
      ],
      [
          33.9719869,
          35.6102066
      ],
      [
          33.9719869,
          35.6102066
      ],
      [
          33.9778204,
          35.6270862
      ],
      [
          33.9778204,
          35.6270862
      ],
      [
          33.9848294,
          35.6394782
      ],
      [
          34.4086666,
          35.8183292
      ],
      [
          34.4092845,
          35.8175848
      ]
    ];
    let type: any = 'simulation';

    let color: any;
    this.polylineCoordsRoute = x.map((coord: any) => {
      const element3 = parseFloat(coord[0]);
      const element4 = parseFloat(coord[1]);
      // return transform([element3, element4], 'EPSG:4326', 'EPSG:3857');
      return [element4, element3];
    });
    if (type === 'simulation') {
      color = '#FF0000';
    } else if (type === 'direction') {
      color = '#0c1a10';
    } else if (type === 'directionByTime') {
      color = '#3388ff';
    }
    const styles = [
      new Style({
        fill: new Fill({
          color: color,
        }),
        stroke: new Stroke({
          color: color,
          width: 3,
        }),
        image: new CircleStyle({
          radius: 7,
          fill: new Fill({
            color: color,
          }),
        }),
      }),
    ];
    if (this.coordinatesArray.find((a) => a.ID === this.polylineCoordsRoute)) {
    } else if (type === 'direction' || type === 'directionByTime') {
    } else {
     
    }
    this.sourcePolyline = new VectorSource({
      features: [new Feature(new LineString(this.polylineCoordsRoute))],
    });
    this.layerPolyline = new VectorLayer({
      source: this.sourcePolyline,
      style: styles,
    });
    this.polylines.push(this.layerPolyline);
    this.map.addLayer(this.layerPolyline);

    this.markerFeature = new Feature({
      type: 'icon',
      geometry: new Point(this.polylineCoordsRoute[0]), // Starting point for the marker
      // ... other properties for your marker style
    });

    // Create a vector source and layer for the marker
    const markerSource = new VectorSource({
      features: [this.markerFeature],
    });

    const markerLayer = new VectorLayer({
      source: markerSource,
      // Add your marker style here
    });

    // Add the marker layer to the map
    this.map.addLayer(markerLayer);
    $('#animatonbar').css('display', '');
  }

  animateMarker() {
    this.animationInterval = setInterval(() => {
      if (this.currentIndex < this.polylineCoordsRoute.length) {
        // Move the marker to the next coordinate along the line string
        this.markerFeature
          .getGeometry()
          .setCoordinates(this.polylineCoordsRoute[this.currentIndex]);
        this.currentIndex++;
      } else {
        clearInterval(this.animationInterval); // Stop the animation when reached the end
      }
    }, 1000 / this.animationSpeed); // Adjust interval based on speed
  }

  startAnimation() {
    this.animateMarker();
  }

  // Stop animation function triggered by the Stop button
  stopAnimation() {
    clearInterval(this.animationInterval);
  }


  displaybtsrefresh(numArr2: any) {


    if ((window.parent.parent.parent[7] as any) && (window.parent.parent.parent[7] as any).A_sqlcond) {
      console.log("A_sqlcond", (window.parent.parent.parent[7] as any).A_sqlcond)
      this.isSqlCond = (window.parent.parent.parent[7] as any).A_sqlcond;
      console.log("A_sqlcond>>", this.isSqlCond)

    }


 
    if (typeof (window.parent.parent.parent[7] as any).A_sqlcond == "undefined") {
      this.isSqlCond = "";
    }
    
    this.BTSObject= {
      sqlcond:this.isSqlCond,
      ids:numArr2
    }
    this.datacrowdService
      .getfixedelementsObject2BTS(JSON.stringify(this.BTSObject), this.usercode)
      .then((res) => {
        console.log('getfixedelementsObject2BTS>>>>', res);
        //localStorage.setItem('getfixedelementsObject2BTS',JSON.stringify(res));

        this.CdrData = res;

        console.log('x', this.CdrData);
        console.log('x000', this.CdrData[0]);

        this.CdrRowData = [];
        let src: any;

        for (let i = 0; i < this.CdrData[0].length; i++) {
          this.displayBTS(this.CdrData[0][i].BTS, this.CdrData[0][i].INFO);
          console.log('this.CdrData[0][i]>>>', this.CdrData[0][i].BTS);

          for (let j = 0; j < this.CdrData[0][i].SECTORS.length; j++) {
            this.drawarc(
              Number(this.CdrData[0][i].BTS.LATITUDE),
              Number(this.CdrData[0][i].BTS.LONGITUDE),
              this.SectorMeter,
              Number(this.CdrData[0][i].SECTORS[j]),
              Number(this.CdrData[0][i].SECTORS[j]),
              this.SectorColor,
              '',
              this.CdrData[0][i].INFO
            );
          }
        }
        
        this.sourcebts = new VectorSource({
          features: this.fixedBtsArray,
        });
        console.log('source>>>', this.source);

        this.clusterSourcebts = new Cluster({
          minDistance: 100,
          source: this.sourcebts,
        });
        console.log('clusterSource>>>>>>>>', this.clusterSource);
        const styleCache: { [key: number]: Style } = {};

        this.fixedbtsGroup = new VectorLayer({
          source: this.clusterSourcebts,
          zIndex: 9999,
          style: function (bts) {
            // console.log("cluster:::::",bts);
            const size = bts.get('features').length;
            // console.log('size::::',size);
            let style = styleCache[size];
            if (!style) {
              src = '/cybercrowd/angular-offline/assets/icons/BTS.png';

              styleCache[size] = style;
            }

            return new Style({
              image: new Icon({
                src: src,
                scale: 0.8,
                // size:[32,32]
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({ color: '#fff' }),
              }),
            });
          },
        });

        this.fixedbtsGroup.set('A_Type', 'tcd');
        this.fixedbtsGroupArray.push(this.fixedbtsGroup);
        this.map.addLayer(this.fixedbtsGroup);
        this.displayarc();

        this.isTcd = false;
      });
  }




  async   displayManySimulation() {

    let AlocSimulId = (window.parent.parent.parent[7] as any).AlocSimulId;
    if (AlocSimulId == "null") {
      AlocSimulId = "";
    }
    console.log('window.parent.parent.parent[7] as any)', window.parent.parent.parent[7] as any);
    console.log("Asimulationid>>>>>", AlocSimulId.split(",").map(Number));
    AlocSimulId = AlocSimulId.split(",").map(Number)
    for (let k = 0; k < AlocSimulId.length; k++) {
      console.log("i>>>", AlocSimulId[k]);
      if ((window.parent as any) && (window.parent as any).AV2_userCode) {
        this.usercode = (window.parent as any).AV2_userCode;
      }
      await this.datacrowdService.getsimualtion(AlocSimulId[k], this.usercode).then((res) => {
        this.datajson = res;
        console.log("getsimultion response >>>>", this.datajson);
      });
      console.log("this.datajson.markerPositions>>>>", this.datajson.markerPositions);

      if (this.datajson.markerPositions !== null) {
        console.log("this.datajson.markerPositions<<<>>>>>", this.datajson.markerPositions.length);
        this.displayClusters(this.datajson);
        console.log("displayClusters----------", this.displayClusters(this.datajson));
      }

      await this.datacrowdService.getExecutionParam(AlocSimulId[k]).then((res) => {
        this.ExecutionParam = res;
        console.log('resssssssssssssss----------------',res);
        console.log("getExecutionParam response >>>>", this.ExecutionParam.Coordinates);
        let circlecoord = this.ExecutionParam.Coordinates;
        console.log('circlecoord----------------',circlecoord);

        localStorage.setItem("coordsimul", JSON.stringify(circlecoord));

        for (var j = 0;j < circlecoord.length;j++) {
          if (circlecoord[j].Type == "Circle") {

            this.displayCircle(circlecoord[j].center.lng, circlecoord[j].center.lat, circlecoord[j].radius, circlecoord[j].leafletid);
          }
          else if (circlecoord[j].Type == "Polygon") {
            this.displayPolygon(circlecoord);
          }
          else if (circlecoord[j].Type == "Polyline") {
            this.displayPolyline(circlecoord,'simulation');
          }
        }
      });

    }
  }

  closefixedelementpopup(){
    this.fixedelementpopup=false;
    $('#fixedelementpopup').css('display', 'none');
  }

  isPointInsidePolygon(point: any, polygonCoordinates: any): boolean {
    const polygon = new Polygon([polygonCoordinates]);
    return polygon.intersectsCoordinate(point);
  }
  
  getRegionForCoordinates(shapecoordinate:any): string | null {
   
    console.log('this.regiondata>>>>',this.regiondata)
    if (this.regiondata) {
      const geojsonFormat = new GeoJSON();
      const features = geojsonFormat.readFeatures(this.regiondata);
      console.log('features',features)
      features.forEach((feature:any,key:any)=>{
        console.log('feature',feature);
        console.log('feature Geometry ',feature.getGeometry());
        console.log('feature Geometry getCoordinates ',feature.getGeometry().getCoordinates());
  
    let geometry:any = feature.getGeometry().getCoordinates();
    // geometry.forEach((elt:any)=>{
    //   // console.log('elt----',elt);
    //   // console.log('elt000000000----',elt[0]);
    //   elt[0].forEach((elt2:any)=>{
    //     // console.log('elt222222----',elt2);
    //     this.coordinatepolygons.push(elt2);

    //   });

    // });
    // console.log('coordinatepolygons----',this.coordinatepolygons);

  // console.log('geometry type>>>',geometry.getType());
  console.log('geometry region1111111111111111111>>>',feature.getProperties()['iso_a2']);//     var poly2 = turf.polygon(shapecoordinate);
      var poly1 = turf.polygon(this.coordinatepolygons);
      var poly2 = turf.polygon(shapecoordinate);
      var intersection = turf.intersect(poly1, poly2);
  console.log('intersection>>>',intersection);
  
      });
    }
  
    return null;
  }
  
   getSubregionForCoordinates(coordinates: [number, number]): string | null {
    console.log('this.regiondata>>>>',this.regiondata)
  
    if (this.regiondata) {
      const geojsonFormat = new GeoJSON();
      const features = geojsonFormat.readFeatures(this.regiondata);
  
      for (const feature of features) {
        const geometry :any= feature.getGeometry();
        if (geometry.getType() === 'Polygon' && geometry.intersectsCoordinate(coordinates)) {
          return feature.get('subregion');
        }
      }
    }
  
    return null;
  }


  changeElemnt(id:any){
    console.log("id-----",id);
    this.typeElement=id;
    }
    onSaveMarker(event: any){
      this.dialog.closeAll();
      const dialogRef =  this.dialog.open(this.saveMarker);
      
      dialogRef.afterClosed().subscribe(result => {});
      console.log('elementName-----',this.elementName);
      console.log('changeElemnt-----',this.changeElemnt);
      console.log('searchlng-----',this.searchlng);
      console.log('searchlat-----',this.searchlat);
  
    }
  
  closedialogall() {
    this.dialog.closeAll();
  }

  openElementPopup(){
    Swal.fire({
      showCancelButton: true,
      backdrop: false,
      confirmButtonText: 'Save',
      cancelButtonText: 'Execute',
    }).then((res)=>{
      if (res.isConfirmed) {
      console.log("save button");
      this.searchelement=true;

      }
    });
  }
    closeelementpopup(){


    }

    drawroute(){
let x:any=[
  [
      34.4085117,
      35.8184616
  ],
  [
      34.4204491,
      35.8308841
  ],
  [
      34.4204491,
      35.8308841
  ],
  [
      34.420858,
      35.8280376
  ],
  [
      34.420858,
      35.8280376
  ],
  [
      34.4245409,
      35.8270469
  ],
  [
      34.4245409,
      35.8270469
  ],
  [
      34.4357441,
      35.831
  ],
  [
      34.4357441,
      35.831
  ],
  [
      34.4346381,
      35.8323361
  ],
  [
      34.4346381,
      35.8323361
  ],
  [
      34.443753,
      35.8225922
  ],
  [
      34.443753,
      35.8225922
  ],
  [
      34.4414816,
      35.8196738
  ],
  [
      34.4414816,
      35.8196738
  ],
  [
      34.4323681,
      35.8207715
  ],
  [
      34.4323681,
      35.8207715
  ],
  [
      34.4231876,
      35.8233785
  ],
  [
      34.4231876,
      35.8233785
  ],
  [
      34.4153362,
      35.8221897
  ],
  [
      34.4153362,
      35.8221897
  ],
  [
      34.4090163,
      35.8191227
  ],
  [
      34.4090163,
      35.8191227
  ],
  [
      34.4084307,
      35.8185404
  ],
  [
      34.4084307,
      35.8185404
  ],
  [
      34.4091594,
      35.8179927
  ],
  [
      34.4091594,
      35.8179927
  ],
  [
      34.4093697,
      35.8186213
  ],
  [
      34.4093697,
      35.8186213
  ],
  [
      34.4095632,
      35.8178577
  ],
  [
      34.4095632,
      35.8178577
  ],
  [
      34.4086495,
      35.8183912
  ],
  [
      34.4086495,
      35.8183912
  ],
  [
      34.420939,
      35.8310906
  ],
  [
      34.420939,
      35.8310906
  ],
  [
      34.4208417,
      35.8257852
  ],
  [
      34.4208417,
      35.8257852
  ],
  [
      34.4230102,
      35.8238277
  ],
  [
      34.4230102,
      35.8238277
  ],
  [
      34.4352673,
      35.8202117
  ],
  [
      34.4352673,
      35.8202117
  ],
  [
      34.4439361,
      35.8225362
  ],
  [
      34.4439361,
      35.8225362
  ],
  [
      34.4501156,
      35.8342366
  ],
  [
      34.4501156,
      35.8342366
  ],
  [
      34.4547608,
      35.8585475
  ],
  [
      34.4547608,
      35.8585475
  ],
  [
      34.4516249,
      35.8614708
  ],
  [
      34.4516249,
      35.8614708
  ],
  [
      34.4504192,
      35.8603381
  ],
  [
      34.4504192,
      35.8603381
  ],
  [
      34.4545835,
      35.8691024
  ],
  [
      34.4545835,
      35.8691024
  ],
  [
      34.458914,
      35.883867
  ],
  [
      34.458914,
      35.883867
  ],
  [
      34.4629376,
      35.8913061
  ],
  [
      34.4629376,
      35.8913061
  ],
  [
      34.4637315,
      35.9063679
  ],
  [
      34.4637315,
      35.9063679
  ],
  [
      34.4913887,
      35.9551604
  ],
  [
      34.4913887,
      35.9551604
  ],
  [
      34.5065183,
      35.9656054
  ],
  [
      34.5409034,
      36.1890166
  ],
  [
      34.5403982,
      36.1894456
  ],
  [
      34.5403982,
      36.1894456
  ],
  [
      34.541024,
      36.1891756
  ],
  [
      34.541024,
      36.1891756
  ],
  [
      34.5410533,
      36.1899677
  ],
  [
      34.5410533,
      36.1899677
  ],
  [
      34.5410352,
      36.1892673
  ],
  [
      34.4085129,
      35.8184025
  ],
  [
      34.40914,
      35.8175404
  ],
  [
      34.40914,
      35.8175404
  ],
  [
      34.4091619,
      35.8185794
  ],
  [
      34.4091619,
      35.8185794
  ],
  [
      34.4091781,
      35.8174972
  ],
  [
      34.4091781,
      35.8174972
  ],
  [
      34.4085996,
      35.8183996
  ],
  [
      34.4085154,
      35.8184069
  ],
  [
      34.4085154,
      35.8184069
  ],
  [
      34.4086458,
      35.8183915
  ],
  [
      34.4072264,
      35.8194916
  ],
  [
      34.4072264,
      35.8194916
  ],
  [
      34.442156,
      35.8272058
  ],
  [
      34.442156,
      35.8272058
  ],
  [
      34.4397479,
      35.830455
  ],
  [
      34.4397479,
      35.830455
  ],
  [
      34.4365072,
      35.8314599
  ],
  [
      34.4365072,
      35.8314599
  ],
  [
      34.435692,
      35.8309189
  ],
  [
      34.435692,
      35.8309189
  ],
  [
      34.4020062,
      35.8387689
  ],
  [
      34.4020062,
      35.8387689
  ],
  [
      34.4184884,
      35.8313725
  ],
  [
      34.4184884,
      35.8313725
  ],
  [
      34.414061,
      35.8239946
  ],
  [
      34.414061,
      35.8239946
  ],
  [
      34.4086008,
      35.8184664
  ],
  [
      34.4086008,
      35.8184664
  ],
  [
      34.4092958,
      35.8173968
  ],
  [
      34.4092958,
      35.8173968
  ],
  [
      34.408665,
      35.8184581
  ],
  [
      34.4402948,
      35.8297066
  ],
  [
      34.4356552,
      35.8312445
  ],
  [
      34.4356552,
      35.8312445
  ],
  [
      34.438195,
      35.818994
  ],
  [
      34.438195,
      35.818994
  ],
  [
      34.4164267,
      35.8230606
  ],
  [
      34.4164267,
      35.8230606
  ],
  [
      34.3988144,
      35.8095078
  ],
  [
      34.3988144,
      35.8095078
  ],
  [
      34.3673726,
      35.7537024
  ],
  [
      34.3673726,
      35.7537024
  ],
  [
      34.2782216,
      35.6929582
  ],
  [
      34.2782216,
      35.6929582
  ],
  [
      34.2103666,
      35.6517605
  ],
  [
      34.2103666,
      35.6517605
  ],
  [
      34.1405867,
      35.6388547
  ],
  [
      34.1405867,
      35.6388547
  ],
  [
      34.0070014,
      35.6491578
  ],
  [
      34.0070014,
      35.6491578
  ],
  [
      33.9918597,
      35.6433006
  ],
  [
      33.9918597,
      35.6433006
  ],
  [
      33.9859066,
      35.6400205
  ],
  [
      33.9859066,
      35.6400205
  ],
  [
      33.9840561,
      35.6383811
  ],
  [
      33.9840561,
      35.6383811
  ],
  [
      33.9781829,
      35.626657
  ],
  [
      33.9781829,
      35.626657
  ],
  [
      33.9786708,
      35.6192495
  ],
  [
      33.9786708,
      35.6192495
  ],
  [
      33.9173545,
      35.5839095
  ],
  [
      33.9173545,
      35.5839095
  ],
  [
      33.8988215,
      35.5682256
  ],
  [
      33.8988215,
      35.5682256
  ],
  [
      33.8956169,
      35.5429811
  ],
  [
      33.8956169,
      35.5429811
  ],
  [
      33.8976484,
      35.4958922
  ],
  [
      33.8976484,
      35.4958922
  ],
  [
      33.8870611,
      35.4938952
  ],
  [
      33.8870611,
      35.4938952
  ],
  [
      33.8806666,
      35.489621
  ],
  [
      33.8806666,
      35.489621
  ],
  [
      33.893088,
      35.4975644
  ],
  [
      33.893088,
      35.4975644
  ],
  [
      33.8945555,
      35.4949575
  ],
  [
      33.8945555,
      35.4949575
  ],
  [
      33.9144756,
      35.5825864
  ],
  [
      33.9144756,
      35.5825864
  ],
  [
      33.9326969,
      35.5891901
  ],
  [
      33.9326969,
      35.5891901
  ],
  [
      33.9472145,
      35.5927684
  ],
  [
      33.9472145,
      35.5927684
  ],
  [
      33.9655616,
      35.6066886
  ],
  [
      33.9655616,
      35.6066886
  ],
  [
      33.9719869,
      35.6102066
  ],
  [
      33.9719869,
      35.6102066
  ],
  [
      33.9778204,
      35.6270862
  ],
  [
      33.9778204,
      35.6270862
  ],
  [
      33.9848294,
      35.6394782
  ],
  [
      34.4086666,
      35.8183292
  ],
  [
      34.4092845,
      35.8175848
  ]
];
this.displayPolyline(x,"bts")
  
    }



    convertCountryCode(inputArray:any){
 
    
    const groupedArray: any[] = [];
    
    inputArray.forEach((item:any) => {
        const matchIndex = groupedArray.findIndex(group => group[0] === item[0] && group[1] === item[1]);
    
        if (matchIndex !== -1) {
            groupedArray[matchIndex][2].push(item[2]);
        } else {
            if (Array.isArray(item[2])) {
                groupedArray.push([item[0], item[1], [...item[2]]]);
            } else {
                groupedArray.push([item[0], item[1], [item[2]]]);
            }
        }
    });
    
    console.log("groupedArray>>",groupedArray);
    return groupedArray;
    }

    convertCountryCode2(inputArray: any) {
      const groupedArray: any[] = [];
    
      inputArray.forEach((item: any) => {
        const matchIndex = groupedArray.findIndex(
          (group) => group[0] === item[0] && group[1] === item[1]
        );
    
        if (matchIndex !== -1) {
          // Check if the third element is an array
          if (Array.isArray(item[2])) {
            groupedArray[matchIndex][2].push(...item[2]); // Use spread to add elements
          } else {
            groupedArray[matchIndex][2].push(item[2]); // Add the single element
          }
        } else {
          if (Array.isArray(item[2])) {
            groupedArray.push([item[0], item[1], [...item[2]]]);
          } else {
            groupedArray.push([item[0], item[1], [item[2]]]);
          }
        }
      });
    
      console.log("groupedArray>>", groupedArray);
      return groupedArray;
    }
    
    devaddgroup(){
   
      this.devicesArray= this.dataservice1.getDHselectedDevice();
    console.log('opengrp==================',this.devicesArray);
    // let selectedarray=localStorage.getItem('selectedarray');
    // console.log("selectedarray-----------",  selectedarray); 
    // let devaddgrp=localStorage.getItem('deviceselected');
    // let hitsaddgrp=localStorage.getItem('nbhits');

    // this.devaddgrparray.push(devaddgrp);
    // this.hitsaddgrpnb.push(hitsaddgrp);
    // console.log("devaddgrparray-----------",  this.devaddgrparray); 
    // console.log("hitsaddgrpnb-----------",  this.hitsaddgrpnb);
  }

    openaddgrpscreen(){
      console.log("devicesArray=============",this.devicesArray);
      this.devicesArray= [...new Set(this.devicesArray)];
      console.log("devicesArray=============",this.devicesArray);
      this.devaddgrparray=[];
      this.hitsaddgrpnb=[];
      this.devicesArray.forEach((elt:any)=>{
        console.log("deviceid-----------",elt.key);
        console.log("deviceid-----------",elt.allChildrenCount);
        this.devaddgrparray.push(elt.key);
        this.hitsaddgrpnb.push(elt.allChildrenCount);
      });
      console.log("devaddgrparray2222222222-----------",  this.devaddgrparray);
      console.log("hitsaddgrpnb2222222222-----------",  this.hitsaddgrpnb);
      console.log("doAddGrouping-----------",  (window.parent.parent.parent[7] as any).doAddGrouping);

      
      (window.parent.parent.parent[7] as any).doAddGrouping(this.devaddgrparray,this.hitsaddgrpnb);
    }

   
    changebar(){
      if(this.isRunning==false){
     
      }else {
       this.stopTimeline();
       if(this.showbarreverse==true && this.showbarstart==false){
        
         this.ReverseTimeline();
       }else if(this.showbarstart==true && this.showbarreverse==false){
        
         this.startTimeline();
       }
      }
     
     }

     

     createnewSenario(){
      // if(this.SenarioContentdiv==true)
      // this.SenarioContentdiv=true;
      $('#SenarioContentdiv').css('display', '');
      if(this.SenarioContentdiv===true){
        this.SenarioContentdiv=false;
      }
      this.isGreen = !this.isGreen;
      this.senarioFlag=!this.senarioFlag;
      // $(".ShowSenario").css("display","");
      // $(".graphtools").css("top","");
      // $(".actionBtnContainer").css("top","");
      // $(".leaflet-control").css("top","");
   
      if(this.senarioFlag==true){
        // $(".leaflet-draw-section").css("top","33px");
      //  $(".leaflet-top").css("top","34px");
  
      //  $("#ShowSenarioTimeline").css("display","");
      //   $(".toggle_container").css("top","50px");
      //     $("#button-container").css("top","90px");
      //     $(".button-container2 ").css("top","225px");
      //     $(".button-container3 ").css("top","265px");
      //     $(".button-container4 ").css("top","340px");



        // $(".button-container3").css("top","60px");

      //  $(".showtools").css("top","45px");

       
        // $("#moretools").css("top","310px");
        // $("#graphtools").css("top","312px");
  
      }else{
        // $("#ShowSenarioTimeline").css("display","none");
        // $(".toggle_container").css("top","10px");
        //   $("#button-container").css("top","3.5rem");
        //   $(".button-container2 ").css("top","12.5rem");
        //   $(".button-container3 ").css("top","15.5rem");
        //   $(".button-container4 ").css("top","20.5rem");
        // $(".leaflet-draw-section").css("top","0px");
        // $(".leaflet-top").css("top","0px");
  
        // $("#moretools").css("top","281px");
        // $("#graphtools").css("top","284px");
  
        this.addnewsenariocount=0;
        this.senarioParentName=null;
        this.firstsenario=null
        this.SenarioRowData=[];
        this.senariocount=0  ;
        this.addnewsenariocount=0;
        this.internalcode=null;
        let obj22:any={
          Action:"CloseSenario",
        }
          
        // this.senarioIdOutput.emit(obj22);
            this.navbarSimulId=obj22;
      }
  
    }
  
    async OpenSenarioAlert(){
        // if(this.simulationid!=undefined && this.senarioFlag==true){
          // this.SenarioContentdiv=true;
          this.SenarioContentdiv=!this.SenarioContentdiv;
      console.log("SenarioContentdiv---------",this.SenarioContentdiv);

          $('#SenarioContentdiv').css('display', '');

          let parentsimulion:any;
          if(this.addnewsenariocount==0){
            parentsimulion=this.senarioParentName;
          }else if(this.addnewsenariocount>0){
            parentsimulion=this.firstsenario;
          }
          console.log("senarioParentName>>>>>>",this.senarioParentName);
          console.log("firstsenario>>>>>>",this.firstsenario);
          console.log("addnewsenariocount>>>>>>",this.addnewsenariocount);
  
          this.SenarioRowData= await this.datacrowdService.displaysequence(173347);
          console.log("SenarioRowData>>>>>>",this.SenarioRowData);
           this.SenarioRowData=JSON.parse(this.SenarioRowData);
        
          // this.columnDefs2 = this.columnDefs2;
          // this.closemodalservice();
          // this.modyalRef = this.modalService.open(this.SenarioContent);
          ///zaher
         


          // const dialogRef =  this.dialog.open(this.SenarioContent);


          // $('.cdk-global-overlay-wrapper').css('display', 'none');
          // $('.ag-grid-container').css('display','');
        // $(".modal-content").css("width", "197px");
        // // $(".modal-content").css("right", "379px");
        // $(".modal-content").css("padding", "10px");
        // $(".modal-content").css("left", "-279px");
        // $(".modal-content").css("top", "17px");
            // $(".modal-content").css("width", "500px");
            // $(".modal-content").css("right", "200px");
            // $(".modal-content").css("padding", "10px");
            // $(".modal-content").css("top", "85px");
          $(".modal-content").draggable({
            axis: "both",
            cursor: "move"
          });
        
  
      // }else{
  
      // }
  
      console.log("senarioFlag in OpenSenarioAlert >>>>>>>>>>>>>>>>>",this.senarioFlag);
     
  
    }
  
  
  
    async addnewSenario(){
      if(this.addnewsenariocount==0){
        this.firstsenario=this.senarioParentName;
  //     await this.datacrowdService.checkSimulationifSaved(this.senarioParentName).then(async (res:any)=>{
  //       console.log("checkSimulationifSaved res",res)
  // if(res=='0'){
  
  //   const dialogRef = this.dialog.open(this.saveSimul);
  
  // }else{
  
  // }
  //      });
      }
      let parentSenario=localStorage.getItem("parentSenario");
      console.log("parentSenario>>>>",parentSenario);
      let internalcode:any ;
       await this.datacrowdService.getinternalcode(parentSenario).then((res:any)=>{
        console.log("res>>>>>>>>>>>",res);
        this.internalcode=res[0];
        this.senariocount=res[1];
  
      });
      this.senariocount++;
      console.log("senariocount>>>>>>>>>>>",this.senariocount);
      console.log("internalcode>>>>>>>>>>>",this.internalcode);
  
   this.senarioParentName=parentSenario;
   this.addnewsenariocount++;
   console.log("senarioFlag in addnewSenario>>>>>>>>>>>",this.senarioFlag);
  
  
    }
  //   async displaysenario(event:any){
  //     console.log("event>>>>>>>>>>>",event);
  //     this.simulationid=event;
  //     this.clearShapes();
  //     this.displayClusters2(event);
  //     // this.displayShapes(event);
  
  //     await this.datacrowdService.getExecutionParam(event).then((res:any)=>{
  // console.log("getExecutionParam",res);
  // if((window.parent.parent.parent[7] as any)){
  //   (window.parent.parent.parent[7] as any).ChangeHeaderParameter(res);
  
  // }
  //     });
  //   }

    async displaysenario(event:any){
      //console.log("event>>>>>>>>>>>",event);
      this.simulationid=event;
      this.clearShapes();
      await this.datacrowdService.getExecutionParam(event).then((res:any)=>{
          console.log("getExecutionParam111",res);
          this.Senario_reportType=res.reportType;
          let shapecoord = res.Coordinates;
          this.BtsTypeSlected=res.BtsTypeSlected;

          localStorage.setItem('coordsimul', JSON.stringify(shapecoord));
          var shapeId = 0;

          for (var j = 0; j < shapecoord.length; j++) {
            if (shapecoord[j].Type == 'Circle') {
              this.displayCircle(
                shapecoord[j].center.lng,
                shapecoord[j].center.lat,
                shapecoord[j].radius,
                shapecoord[j].leafletid
              );
            } else if (shapecoord[j].Type == 'Polygon') {
              this.displayPolygon(shapecoord[j]);
            } else if (shapecoord[j].Type == 'Polygon') {
              this.displayPolyline(shapecoord[j], 'simulation');
            }
          }

        if((window.parent.parent.parent[7] as any)){
          (window.parent.parent.parent[7] as any).ChangeHeaderParameter(res);
        
        }
            });
      if (this.Senario_reportType == 11) {
        await this.datacrowdService.getsimualtion(this.simulationid, this.usercode).then((res) => {
          this.datajson = res;
          console.log("getsimultion response >>>>", this.datajson);
  
        });
        
        $('#controlbutton').css('display', '');
        this.tcd();
  
      } if (this.Senario_reportType == 8 ||this.Senario_reportType == 9 )  {
        let numArr2: any = [];
        if ( this.BtsTypeSlected=='BTS') {
   

              this.datacrowdService
                .getVcisfixedelementsID(this.simulationid)
                .then(async (response2: any) => {
                  response2 = response2;
                  if (response2.length === 0) {
                    Swal.fire({
                      text: 'No fixed Elements Available',
                      backdrop: false,
                      // showCancelButton: true,
                    });
                  }

                  console.log('getVcisfixedelementsID>>>', response2);
                  console.log('in fixed element scan');

                  for (var i = 0; i < response2.length; i++) {
                    console.log(response2[i][0]);
                    numArr2.push(Number(response2[i][0]));
                  }
                  console.log('numArr>>', numArr2);

                  if (
                    (window.parent as any) &&
                    (window.parent as any).AV2_userCode
                  ) {
                    this.usercode = (window.parent as any).AV2_userCode;
                  }

                  this.displaybtsrefresh(numArr2);

                  if (this.reportType == 9) {
                    console.log('in fixed element activity scan');
                    await this.datacrowdService
                      .getsimualtion(event,this.usercode)
                      .then((res) => {
                        this.datajson = res;
                        if (this.datajson.markerPositions.length === 0) {
                          Swal.fire({
                            text: 'No Data Available',
                            backdrop: false,
                            // showCancelButton: true,
                          });
                          (window.parent.parent.parent[7] as any).clearshapes();
                        } else {
                          console.log(
                            'getsimultion response >>>>',
                            this.datajson
                          );

                          const tooltipElements3 =
                            this.el.nativeElement.querySelectorAll(
                              'div[id="dateTooltip"]'
                            );
                          tooltipElements3.forEach((element: HTMLElement) => {
                            if (element.parentNode) {
                              this.renderer.setStyle(
                                element,
                                'display',
                                'none'
                              );
                            }
                          });
                          // this.addheats(this.datajson);
                          for (
                            let i = 0;
                            i < this.datajson.markerPositions.length;
                            i++
                          ) {
                            let point = new Point([
                              parseFloat(this.datajson.markerPositions[i][1]),
                              parseFloat(this.datajson.markerPositions[i][0]),
                            ]);
                            this.marker = new Feature({
                              geometry: point,
                              name: 'Marker',
                            });
                            this.marker.set('type', 'marker');
                            this.marker.set(
                              'info',
                              this.datajson.markerPositions[i]
                            );
                            this.clusterFeatures.push(this.marker);
                          }

                          this.source = new VectorSource({
                            features: this.clusterFeatures,
                          });
                          this.clusterSource = new Cluster({
                            source: this.source,
                          });

                          const styleCache: { [key: number]: Style } = {};

                          this.clusters = new VectorLayer({
                            source: this.clusterSource,
                            style: (feature) => {
                              const size = feature.get('features').length;
                              let style = styleCache[size];
                              if (!style) {
                                if (size < 20) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#2EFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#2EFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 20 && size < 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FFFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#FFFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FF660040',
                                      }),
                                      fill: new Fill({
                                        color: '#FF660090',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                }
                                styleCache[size] = style;
                              }
                              return style;
                            },
                          });

                          this.map.addLayer(this.clusters);
                          if (this.reportType == 2) {
                            this.clustersZoom(this.datajson);
                          }
                          this.clustersZoom(this.datajson);
                        }
                      });
                  }

                  this.map.addInteraction(select);
                  numArr2 = [];
                });
          
        } else if (this.BtsTypeSlected=='Fixed Element') {

              this.datacrowdService
                .getVcisfixedelementsID(this.simulationid)
                .then(async (response2: any) => {
                  response2 = response2;
                  if (response2.length === 0) {
                    Swal.fire({
                      text: 'No fixed Elements Available',
                      backdrop: false,
                      // showCancelButton: true,
                    });
                  }

                  console.log('getVcisfixedelementsID>>>', response2);
                  console.log('in fixed element scan');

                  for (var i = 0; i < response2.length; i++) {
                    numArr2.push(Number(response2[i][0]));
                  }
                  console.log('numArr>>', numArr2);

                  if (
                    (window.parent as any) &&
                    (window.parent as any).AV2_userCode
                  ) {
                    this.usercode = (window.parent as any).AV2_userCode;
                  }

                  this.datacrowdService
                    .getfixedelementsObject(numArr2)
                    .then((res) => {
                      console.log('getfixedelementsObject>>>>', res);
                      localStorage.setItem(
                        'fixedelementsObject',
                        JSON.stringify(res)
                      );
                      this.displayFixedElements(res);
                    });

                  if (this.reportType == 9) {
                    console.log('in fixed element activity scan');
                    await this.datacrowdService
                    .getsimualtion(event,this.usercode)
                      .then((res) => {
                        this.datajson = res;
                        if (this.datajson.markerPositions.length === 0) {
                          Swal.fire({
                            text: 'No Data Available',
                            backdrop: false,
                            // showCancelButton: true,
                          });
                          (window.parent.parent.parent[7] as any).clearshapes();
                        } else {
                          console.log(
                            'getsimultion response >>>>',
                            this.datajson
                          );

                          const tooltipElements3 =
                            this.el.nativeElement.querySelectorAll(
                              'div[id="dateTooltip"]'
                            );
                          tooltipElements3.forEach((element: HTMLElement) => {
                            if (element.parentNode) {
                              this.renderer.setStyle(
                                element,
                                'display',
                                'none'
                              );
                            }
                          });
                          // this.addheats(this.datajson);
                          for (
                            let i = 0;
                            i < this.datajson.markerPositions.length;
                            i++
                          ) {
                            let point = new Point([
                              parseFloat(this.datajson.markerPositions[i][1]),
                              parseFloat(this.datajson.markerPositions[i][0]),
                            ]);
                            this.marker = new Feature({
                              geometry: point,
                              name: 'Marker',
                            });
                            this.marker.set('type', 'marker');
                            this.marker.set(
                              'info',
                              this.datajson.markerPositions[i]
                            );
                            this.clusterFeatures.push(this.marker);
                          }

                          this.source = new VectorSource({
                            features: this.clusterFeatures,
                          });
                          this.clusterSource = new Cluster({
                            source: this.source,
                          });

                          const styleCache: { [key: number]: Style } = {};

                          this.clusters = new VectorLayer({
                            source: this.clusterSource,
                            style: (feature) => {
                              const size = feature.get('features').length;
                              let style = styleCache[size];
                              if (!style) {
                                if (size < 20) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#2EFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#2EFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 20 && size < 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FFFF2E40',
                                      }),
                                      fill: new Fill({
                                        color: '#FFFF2E90',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                  styleCache[size] = style;
                                } else if (size >= 100) {
                                  style = new Style({
                                    image: new CircleStyle({
                                      radius: 20,
                                      stroke: new Stroke({
                                        width: 10,
                                        color: '#FF660040',
                                      }),
                                      fill: new Fill({
                                        color: '#FF660090',
                                      }),
                                    }),
                                    text: new Text({
                                      text: size.toString(),
                                      fill: new Fill({
                                        color: '#000',
                                      }),
                                    }),
                                  });
                                }
                                styleCache[size] = style;
                              }
                              return style;
                            },
                          });

                          this.map.addLayer(this.clusters);
                          if (this.reportType == 2) {
                            this.clustersZoom(this.datajson);
                          }
                          this.clustersZoom(this.datajson);
                        }
                      });
                  }

                  this.map.addInteraction(select);
                  numArr2 = [];
                });
        }
      }
        else{
        this.displayClusters2(event);
        // this.displayShapes(event);
    
  
      }
   
    }


    async displayClusters2(simulid: any) {
      await this.datacrowdService.getsimualtion(simulid, this.usercode).then((res) => {
        this.datajson = res;
        console.log("getsimultion response >>>>", this.datajson);
      })
      console.log("this.datajson.markerPositions>>>>", this.datajson.markerPositions);
  let datajs=this.datajson;

      this.displayclusters=true;
      for (let i = 0; i < datajs.markerPositions.length; i++) {
        let point = new Point([
          parseFloat(datajs.markerPositions[i][1]),
          parseFloat(datajs.markerPositions[i][0]),
        ]);
        this.marker = new Feature({
          geometry: point,
          name: 'Marker',
        });
        this.marker.set('type', 'marker');
        this.marker.set('info', datajs.markerPositions[i]);
        this.clusterFeatures.push(this.marker);
      }
      this.clusterFeaturesLoop=this.clusterFeatures;
      this.source = new VectorSource({
        features: this.clusterFeatures,
      });
      this.clusterSource = new Cluster({
        source: this.source,
      });

      this.sourceLoop = new VectorSource({
        features: this.clusterFeatures,
      });
      this.clusterSourceLoop = new Cluster({
        source: this.sourceLoop,
      });

      const styleCache: { [key: number]: Style } = {};
  
      this.clusters = new VectorLayer({
        source: this.clusterSource,
        style: (feature) => {
          const size = feature.get('features').length;
          let style = styleCache[size];
          if (!style) {
            if (size < 20) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#2EFF2E40',
                  }),
                  fill: new Fill({
                    color: '#2EFF2E90',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            } else if (size >= 20 && size < 100) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#FFFF2E40',
                  }),
                  fill: new Fill({
                    color: '#FFFF2E90',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            } else if (size >= 100) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#FF660040',
                  }),
                  fill: new Fill({
                    color: '#FF660090',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
            }
            styleCache[size] = style;
          }
          return style;
        },
      });
      
      this.clustersLoop = new VectorLayer({
        source: this.clusterSourceLoop,
        style: (feature) => {
          const size = feature.get('features').length;
          let style = styleCache[size];
          if (!style) {
            if (size < 20) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#2EFF2E40',
                  }),
                  fill: new Fill({
                    color: '#2EFF2E90',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            } else if (size >= 20 && size < 100) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#FFFF2E40',
                  }),
                  fill: new Fill({
                    color: '#FFFF2E90',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
              styleCache[size] = style;
            } else if (size >= 100) {
              style = new Style({
                image: new CircleStyle({
                  radius: 20,
                  stroke: new Stroke({
                    width: 10,
                    color: '#FF660040',
                  }),
                  fill: new Fill({
                    color: '#FF660090',
                  }),
                }),
                text: new Text({
                  text: size.toString(),
                  fill: new Fill({
                    color: '#000',
                  }),
                }),
              });
            }
            styleCache[size] = style;
          }
          return style;
        },
      });

      this.clusters.set('type', 'marker');
      this.manysimularray.push(this.clusters);
      this.manysimularrayLoop.push(this.clustersLoop);
      this.map.addLayer(this.clusters);
      this.magnifiedMap.addLayer(this.clustersLoop);

      if (this.reportType == 2) {
        this.clustersZoom(datajs);
      }
  
  
      this.map.on('singleclick', (event) => {
        // get the feature you clicked
        const feature = this.map.forEachFeatureAtPixel(
          event.pixel,
          (feature: any) => {
            return feature;
          }
        );
      });
    }

    receiveData(data: string) {
      //alert("receivedata")
      console.log(data);
      // Do something with the data
    }

 
        findCoTraveler() {
          (window.parent.parent.parent[8] as any).closepopup();
          this.coTravelerId = (window.parent.parent.parent[7] as any).A_reportId;
          if (this.coTravelerId == "null") {
            this.coTravelerId = "";
          }
          this.beginOperation = (window.parent.parent.parent[7] as any).A_beginOperation;
          if (this.beginOperation == "null") {
            this.beginOperation = "";
          }
      
          this.endOperation = (window.parent.parent.parent[7] as any).A_endOperation;
          if (this.endOperation == "null") {
            this.endOperation = "";
          }
      
          this.deviceIdArr = (window.parent.parent.parent[7] as any).A_deviceIdArr;
          if (this.deviceIdArr == "null") {
            this.deviceIdArr = "";
          }
          const body = { "beginOperation": this.beginOperation, "endOperation": this.endOperation, "deviceIdArr": this.deviceIdArr, "reportId": this.coTravelerId };
          let response = this.executeCoTraveler(body);
      
        }
        // notification(type :any,message:any){
        //   this.notif.showtoast(type,message);
        // }
    
        executeCoTraveler(obj: any) {
          try {
            // this.notification('info','Processing Co-Traveler ');
           this.datacrowdService.executeCoTraveler(obj);
            // this.loaderService.hide();
            this.counter = 0;
            this.checkCoTravelerCounter = 0;
            this.coTravelerSubscription = interval(1000).pipe(
              switchMap(() => this.datacrowdService.checkTableCoTravelers(this.coTravelerId, this.checkCoTravelerCounter))
            )
              .subscribe(data => {
                this.checkCoTravelerCounter++;
                // this.loaderService.hide();
                if (data != 0) {
                  this.coTravelerSubscription.unsubscribe();
                //  this.notification('success', 'CO Travelers are ready');
                  // const dialogRef = this.dialog.open(this.CoTravelersReady);
                  Swal.fire({
                    text: 'Do You Want To Display Co-Traveler?',
                    showCancelButton: true,
                    backdrop: false,
                    confirmButtonText: 'Ok',
                  }).then((result) => {
                    if (result.isConfirmed) {
                      this.DisplayCoTravelerflag=1;
                      this.displayCoTravelers();
                    } else {
                      this.dialog.closeAll();
                    }
                  });
      
                }
              });
      
      
          } catch (error: any) {
            // this.notification('error', 'CO Travelers Failure');
            console.log("failure exception", error);
            console.log("Fail - Please contact valoores team");
          }
        }

      

       async displayCoTravelers() {
          this.deviceIdArr = (window.parent.parent.parent[7] as any).A_deviceIdArr;
    if (this.deviceIdArr == "null") {
      this.deviceIdArr = "";
    }
    this.coTravelerId = (window.parent.parent.parent[7] as any).A_reportId;
    if (this.coTravelerId == "null") {
      this.coTravelerId = "";
    }
    if(this.DisplayCoTravelerflag == 1){
      this.deviceCoordinatesArr = await this.datacrowdService.getAllCoTravelerCommonLocationHits(this.coTravelerId);
    }else if(this.DisplayCoTravelerflag == 2){
      this.coTravelerId = this.dataservice1.getTimelineSimulID(); 
     this.deviceIdArr = this.dataservice1.getDeviceCommonLocationHits();
      this.deviceCoordinatesArr = await this.datacrowdService.getCommonLocationHits(this.coTravelerId,this.deviceIdArr);
    }else{
    this.deviceCoordinatesArr =await this.datacrowdService.getCoTravelerCommonLocationHits(this.coTravelerId,this.deviceIdArr);
    }

    const body = { "deviceIdArr": this.deviceIdArr, "deviceCoordinatesArr": this.deviceCoordinatesArr };
    console.log("33333333333333",body);
      
          // this.deviceCoordinatesArr = [
          //   '[["33.29859566", "44.33047591", "dd78ae81-0441-494a-bfff-fad32d631478", 1634591370000, "Canvas Eclair muh 6159", "9"], ["33.2985946", "44.33050605", "dd78ae81-0441-494a-bfff-fad32d631478", 1634591401000, "Canvas Eclair muh 6159", "9"], ["33.29857688", "44.33050151", "dd78ae81-0441-494a-bfff-fad32d631478", 1634591440000, "Canvas Eclair muh 6159", "9"], ["33.2985802", "44.33049946", "dd78ae81-0441-494a-bfff-fad32d631478", 1634592826000, "Canvas Eclair muh 6159", "9"], ["33.29856254", "44.3304974", "dd78ae81-0441-494a-bfff-fad32d631478", 1634594122000, "Canvas Eclair muh 6159", "9"], ["33.2985885", "44.33048907", "dd78ae81-0441-494a-bfff-fad32d631478", 1649024628000, "Canvas Eclair muh 6159", "9"], ["33.29859083", "44.33050871", "dd78ae81-0441-494a-bfff-fad32d631478", 1649025676000, "Canvas Eclair muh 6159", "9"], ["33.2985839", "44.33050295", "dd78ae81-0441-494a-bfff-fad32d631478", 1649026010000, "Canvas Eclair muh 6159", "9"], ["33.29859828", "44.33048461", "dd78ae81-0441-494a-bfff-fad32d631478", 1654899441000, "Canvas Eclair muh 6159", "9"], ["33.2985792", "44.33049542", "dd78ae81-0441-494a-bfff-fad32d631478", 1654899508000, "Canvas Eclair muh 6159", "9"], ["33.29858763", "44.33055098", "dd78ae81-0441-494a-bfff-fad32d631478", 1654899569000, "Canvas Eclair muh 6159", "9"], ["33.2986076", "44.33050915", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668241000, "Canvas Eclair muh 6159", "9"], ["33.29858385", "44.33049401", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668272000, "Canvas Eclair muh 6159", "9"], ["33.29859291", "44.33051844", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668387000, "Canvas Eclair muh 6159", "9"], ["33.29859891", "44.33052063", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668454000, "Canvas Eclair muh 6159", "9"], ["33.29856324", "44.33054902", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668511000, "Canvas Eclair muh 6159", "9"], ["33.29858972", "44.33051366", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668538000, "Canvas Eclair muh 6159", "9"], ["33.29855961", "44.33046492", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668811000, "Canvas Eclair muh 6159", "9"], ["33.29859551", "44.33052527", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668862000, "Canvas Eclair muh 6159", "9"], ["33.29860375", "44.33052197", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668955000, "Canvas Eclair muh 6159", "9"], ["33.2985659", "44.33052436", "dd78ae81-0441-494a-bfff-fad32d631478", 1669668975000, "Canvas Eclair muh 6159", "9"], ["33.2985771", "44.33052884", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669070000, "Canvas Eclair muh 6159", "9"], ["33.29858258", "44.3305153", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669161000, "Canvas Eclair muh 6159", "9"], ["33.29860029", "44.33050362", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669269000, "Canvas Eclair muh 6159", "9"], ["33.29858884", "44.33050224", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669429000, "Canvas Eclair muh 6159", "9"], ["33.29860702", "44.33050328", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669815000, "Canvas Eclair muh 6159", "9"], ["33.29856389", "44.33055388", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669901000, "Canvas Eclair muh 6159", "9"], ["33.29860624", "44.33052699", "dd78ae81-0441-494a-bfff-fad32d631478", 1669669997000, "Canvas Eclair muh 6159", "9"], ["33.29855761", "44.33050187", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670163000, "Canvas Eclair muh 6159", "9"], ["33.29857541", "44.33052075", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670164000, "Canvas Eclair muh 6159", "9"], ["33.29855855", "44.3305033", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670492000, "Canvas Eclair muh 6159", "9"], ["33.29857746", "44.33051263", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670510000, "Canvas Eclair muh 6159", "9"], ["33.29857983", "44.33048784", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670549000, "Canvas Eclair muh 6159", "9"], ["33.29856887", "44.33052925", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670705000, "Canvas Eclair muh 6159", "9"], ["33.29859085", "44.33051467", "dd78ae81-0441-494a-bfff-fad32d631478", 1669670889000, "Canvas Eclair muh 6159", "9"], ["33.35587743", "44.41198737", "dd78ae81-0441-494a-bfff-fad32d631478", 1669192726000, "Canvas Eclair muh 6159", "9"]]',
          //   '[["33.31402634", "44.36407091", "6b60b9af-02ef-4ad3-9840-0b4f84c1dcd9", 1641994306000, "Red Cake OIr 3769", "9"]]',
          //   '[["33.31411376", "44.36414602", "5bce9d1b-1f5c-4159-95ce-ef5b03111e27", 1641684205000, "Canvas Sorbet kPo 1875", "9"]]',
          //   '[["33.31402409", "44.36405972", "8c1f3157-3d9b-4c3b-81c4-be6d07725b41", 1641618861000, "Concrete Brulee oxj 2285", "9"], ["33.31400411", "44.36401153", "8c1f3157-3d9b-4c3b-81c4-be6d07725b41", 1641987638000, "Concrete Brulee oxj 2285", "9"]]',
          //   '[["33.3140341", "44.36404467", "3175016f-dbb5-4957-8267-de4942534be2", 1641558191000, "Velvet Coconut Wwn 7164", "9"]]',
          //   '[["33.31408088", "44.36408648", "a0f56e81-59ae-4e9f-9a2c-df90bdcb3615", 1641433688000, "Silk Ice xto 2948", "9"]]',
          //   '[["33.3140218", "44.36402559", "b6ef2812-f05c-4c32-9bf7-7fc7dbb39d47", 1641880200000, "Table Truffle FFV 7869", "9"]]',
          //   '[["33.29859824", "44.33050658", "97ca3497-a37c-457d-812e-acb8e992dc70", 1654203145000, "Turquoise Brownie xAB 1643", "9"], ["33.29862606", "44.33050838", "97ca3497-a37c-457d-812e-acb8e992dc70", 1654205211000, "Turquoise Brownie xAB 1643", "9"], ["33.29862511", "44.33049014", "97ca3497-a37c-457d-812e-acb8e992dc70", 1654206155000, "Turquoise Brownie xAB 1643", "9"], ["33.29858099", "44.33053668", "97ca3497-a37c-457d-812e-acb8e992dc70", 1654206489000, "Turquoise Brownie xAB 1643", "9"], ["33.29858086", "44.33052384", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738430000, "Turquoise Brownie xAB 1643", "9"], ["33.29859343", "44.33052718", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738546000, "Turquoise Brownie xAB 1643", "9"], ["33.29859101", "44.33050466", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738640000, "Turquoise Brownie xAB 1643", "9"], ["33.29856563", "44.33052325", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738735000, "Turquoise Brownie xAB 1643", "9"], ["33.29859451", "44.33052344", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738900000, "Turquoise Brownie xAB 1643", "9"], ["33.29859727", "44.33052984", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738955000, "Turquoise Brownie xAB 1643", "9"], ["33.29859579", "44.33051305", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657738990000, "Turquoise Brownie xAB 1643", "9"], ["33.29856969", "44.33052813", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739090000, "Turquoise Brownie xAB 1643", "9"], ["33.29856917", "44.33051868", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739143000, "Turquoise Brownie xAB 1643", "9"], ["33.29859294", "44.33054365", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739217000, "Turquoise Brownie xAB 1643", "9"], ["33.29859835", "44.33052099", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739269000, "Turquoise Brownie xAB 1643", "9"], ["33.29859804", "44.33052517", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739794000, "Turquoise Brownie xAB 1643", "9"], ["33.29859403", "44.33053827", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739979000, "Turquoise Brownie xAB 1643", "9"], ["33.29859119", "44.33051611", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657739997000, "Turquoise Brownie xAB 1643", "9"], ["33.29857809", "44.33052447", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657740133000, "Turquoise Brownie xAB 1643", "9"], ["33.29857376", "44.33050894", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657740200000, "Turquoise Brownie xAB 1643", "9"], ["33.29857824", "44.33048764", "97ca3497-a37c-457d-812e-acb8e992dc70", 1657740485000, "Turquoise Brownie xAB 1643", "9"]]',
          //   '[["33.29858795", "44.33051662", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1634592261000, "Leather Cannoli DpZ 5280", "9"], ["33.29858833", "44.33047666", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1634594376000, "Leather Cannoli DpZ 5280", "9"], ["33.29857874", "44.33051029", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649024234000, "Leather Cannoli DpZ 5280", "9"], ["33.29861173", "44.3305008", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649024775000, "Leather Cannoli DpZ 5280", "9"], ["33.29862614", "44.33052026", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649025168000, "Leather Cannoli DpZ 5280", "9"], ["33.29861192", "44.33049916", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649025512000, "Leather Cannoli DpZ 5280", "9"], ["33.29856502", "44.33053723", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649027720000, "Leather Cannoli DpZ 5280", "9"], ["33.29855899", "44.33051968", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649027933000, "Leather Cannoli DpZ 5280", "9"], ["33.29860387", "44.33053537", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1649028137000, "Leather Cannoli DpZ 5280", "9"], ["33.29856364", "44.33048163", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1654900036000, "Leather Cannoli DpZ 5280", "9"], ["33.29861866", "44.33050035", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1654900420000, "Leather Cannoli DpZ 5280", "9"], ["33.29860265", "44.33050348", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1654901392000, "Leather Cannoli DpZ 5280", "9"], ["33.29856631", "44.33050911", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668127000, "Leather Cannoli DpZ 5280", "9"], ["33.29858747", "44.33052452", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668376000, "Leather Cannoli DpZ 5280", "9"], ["33.29858268", "44.33052465", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668449000, "Leather Cannoli DpZ 5280", "9"], ["33.29859559", "44.33051703", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668550000, "Leather Cannoli DpZ 5280", "9"], ["33.29858373", "44.33049658", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668557000, "Leather Cannoli DpZ 5280", "9"], ["33.29859495", "44.33051292", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668735000, "Leather Cannoli DpZ 5280", "9"], ["33.29857726", "44.33047885", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669668886000, "Leather Cannoli DpZ 5280", "9"], ["33.29860738", "44.33049108", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669669050000, "Leather Cannoli DpZ 5280", "9"], ["33.29858792", "44.33051817", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669669056000, "Leather Cannoli DpZ 5280", "9"], ["33.29860491", "44.33047759", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669669771000, "Leather Cannoli DpZ 5280", "9"], ["33.29858129", "44.33053185", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669669796000, "Leather Cannoli DpZ 5280", "9"], ["33.29858773", "44.33051414", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669669965000, "Leather Cannoli DpZ 5280", "9"], ["33.29859852", "44.33048778", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670019000, "Leather Cannoli DpZ 5280", "9"], ["33.29857774", "44.33053134", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670191000, "Leather Cannoli DpZ 5280", "9"], ["33.2985823", "44.33052194", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670255000, "Leather Cannoli DpZ 5280", "9"], ["33.29862001", "44.33053691", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670416000, "Leather Cannoli DpZ 5280", "9"], ["33.2985932", "44.3305466", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670459000, "Leather Cannoli DpZ 5280", "9"], ["33.29857642", "44.33050818", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670500000, "Leather Cannoli DpZ 5280", "9"], ["33.29858324", "44.33050972", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670606000, "Leather Cannoli DpZ 5280", "9"], ["33.29857675", "44.33049431", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670736000, "Leather Cannoli DpZ 5280", "9"], ["33.2985701", "44.33053289", "b5a8a1fe-c976-4442-9bd8-bf4e1428beaf", 1669670926000, "Leather Cannoli DpZ 5280", "9"]]',
          // ];
      
          this.fixedMarkersGroup = [];
          this.marker = [];
          const markerInstances: any = []; // Step 1: Define a variable to store the marker instances.
          const styleCache: { [key: number]: Style } = {}; // Move styleCache outside the loop
          let clusterFeatures: any = [];
          let src: any;
          for (const layer of this.fixedMarkersGroupsArray) {
            this.map.removeLayer(layer);
          }
          try {
            if (typeof this.deviceCoordinatesArr === 'string') {
           
              this.deviceCoordinatesArr = JSON.parse(this.deviceCoordinatesArr);
              console.log('deviceCoordinatesArr:', this.deviceCoordinatesArr);
              this.deviceCoordinatesArr.forEach((element2: any, key: any) => {
                console.log('element222222:::::', element2);
      
                this.comarkers = new Feature(
                  new Point([parseFloat(element2[1]), parseFloat(element2[0])])
                );
                console.log(' this.comarkers::;', this.comarkers);
                clusterFeatures.push(this.comarkers); // Create a new array for each marker
              });
              console.log('clusterFeatures>>>', clusterFeatures);
      
              this.source = new VectorSource({
                features: clusterFeatures,
              });
              console.log('source>>>', this.source);
              console.log('sourcelengthhhhhhhhhhhh>>>', this.source.length);
      
              this.clusterSource = new Cluster({
                minDistance: 100,
                source: this.source,
              });
              console.log('clusterSource>>>>>>>>', this.clusterSource);
      
              const styleCache: { [key: number]: Style } = {};
              this.fixedMarkersGroup = new VectorLayer({
                source: this.clusterSource,
                zIndex: 9999,
                style: (e) => {
                  console.log('cluster:::::', e);
                  const size = e.get('features').length;
                  console.log('size::::', size);
                  let style = styleCache[size];
                  if (!style) {
                    if (size === 1) {
                      for (let i = 0; i < this.deviceCoordinatesArr.length; i++) {
                        src =
                          '/cybercrowd/angular-offline/assets/icons/singleperson.png';
                      }
                    } else if (size > 1) {
                      src = '/cybercrowd/angular-offline/assets/icons/group.png';
                    }
                    styleCache[size] = style;
                  }
      
                  return new Style({
                    image: new Icon({
                      src: src,
                      scale: 0.8,
                      // size:[32,32]
                    }),
                    text: new Text({
                      text: size.toString(),
                      fill: new Fill({ color: '#fff' }),
                    }),
                  });
                },
              });
              this.fixedMarkersGroup.set('A_Type', 'cotraveler');
              console.log('this.fixedMarkersGroup:::::', this.fixedMarkersGroup);
              this.map.addLayer(this.fixedMarkersGroup);
              this.fixedMarkersGroupsArray.push(this.fixedMarkersGroup);
            } else {
             
              this.deviceCoordinatesArr.forEach((element: any, key: any) => {
                let parsedJson = element;
                console.log('parsedJson111111111111::::', parsedJson);
      
                // if (JSON.parse(element)) {
                //   parsedJson = JSON.parse(element);
                //   console.log('parsedJson222222222222::::', parsedJson);
                //   console.log('parsedJson.length::::', parsedJson.length);
                // } else {
                //   parsedJson = element;
                //   console.log('parsedJson33333333333333::::', parsedJson);
                //   console.log('parsedJson.length::::', parsedJson.length);
                // }
      
                // parsedJson.forEach((element2: any, key: any) => {
                  // console.log('element222222:::::', element2);
      
                  this.comarkers = new Feature(
                    new Point([parseFloat(parsedJson[1]), parseFloat(parsedJson[0])])
                  );
                  console.log(' this.comarkers::;', this.comarkers);
      
                  this.comarkers.set('info', parsedJson);
                  clusterFeatures.push(this.comarkers); // Create a new array for each marker
                // });
                console.log('clusterFeatures>>>', clusterFeatures);
      
                this.source = new VectorSource({
                  features: clusterFeatures,
                });
                console.log('source>>>', this.source);
                console.log('sourcelengthhhhhhhhhhhh>>>', this.source.length);
      
                this.clusterSource = new Cluster({
                  minDistance: 100,
                  source: this.source,
                });
                console.log('clusterSource>>>>>>>>', this.clusterSource);
      
                const styleCache: { [key: number]: Style } = {};
                this.fixedMarkersGroup = new VectorLayer({
                  source: this.clusterSource,
                  zIndex: 9999,
                  style: (e) => {
                    console.log('cluster:::::', e);
                    const size = e.get('features').length;
                    console.log('size::::', size);
                    let style = styleCache[size];
                    if (!style) {
                      if (size === 1) {
                        for (let i = 0; i < parsedJson.length; i++) {
                          src =
                            '/cybercrowd/angular-offline/assets/icons/singleperson.png';
                        }
                      } else if (size > 1) {
                        src = '/cybercrowd/angular-offline/assets/icons/group.png';
                      }
                      styleCache[size] = style;
                    }
      
                    return new Style({
                      image: new Icon({
                        src: src,
                        scale: 0.8,
                        // size:[32,32]
                      }),
                      // text: new Text({
                      //   text: size.toString(),
                      //   fill: new Fill({ color: '#fff' }),
                      // }),
                    });
                  },
                });
                this.fixedMarkersGroup.set('A_Type', 'cotraveler');
                console.log('this.fixedMarkersGroup:::::', this.fixedMarkersGroup);
            console.error('fixedMarkersGroup>>>>>>>>>>:', this.fixedMarkersGroup);

                this.fixedMarkersGroupsArray.push(this.fixedMarkersGroup);
              });

              this.map.addLayer(this.fixedMarkersGroup);

            }
          } catch (error) {
            console.error('Error parsing JSON:', error);
            // alert('error');
          }
        }

        findCoRelation() {
          (window.parent.parent.parent[8] as any).closepopup();
          this.coRelationId = (window.parent.parent.parent[7] as any).A_reportId;
          if (this.reportId == "null") {
            this.reportId = "";
          }
          this.beginOperation = (window.parent.parent.parent[7] as any).A_beginOperation;
          if (this.beginOperation == "null") {
            this.beginOperation = "";
          }
          this.endOperation = (window.parent.parent.parent[7] as any).A_endOperation;
          if (this.endOperation == "null") {
            this.endOperation = "";
          }
          this.deviceIdArr = (window.parent.parent.parent[7] as any).A_deviceIdArr;
          if (this.deviceIdArr == "null") {
            this.deviceIdArr = "";
          }
          const body = { "beginOperation": this.beginOperation, "endOperation": this.endOperation, "deviceIdArr": this.deviceIdArr, "reportId": this.coRelationId };
          let response = this.executeCoRelation(body);
      
        }
        executeCoRelation(obj: any) {
          try {
            // this.notification('info','Processing Co-Relation');
           this.datacrowdService.executeCoRelation(obj);
            // this.loaderService.hide();
            this.counter = 0;
            this.checkCoRelationCounter = 0;
            this.coRelationSubscription = interval(1000).pipe(
              switchMap(() => this.datacrowdService.checkTableCoRelations(this.coRelationId, this.checkCoRelationCounter))
            )
              .subscribe(data => {
                this.checkCoTravelerCounter++;
                // this.loaderService.hide();
                if (data != 0) {
                  this.coRelationSubscription.unsubscribe();
                  // this.notification('success', 'CO Relation are ready');

                  Swal.fire({
                    text: 'Do You Want To Display Co-Relation?',
                    showCancelButton: true,
                    backdrop: false,
                    confirmButtonText: 'Ok',
                  }).then((result) => {
                    if (result.isConfirmed) {
                      this.DisplayCoRelationflag=1;
                      this.displayCoRelation();
                    } else {
                      this.dialog.closeAll();
                    }
                  });


                  
      
                }
              });
      
      
          } catch (error: any) {
            // this.notification('error', 'CO Relations Failure');
            console.log("failure exception", error);
            console.log("Fail - Please contact valoores team");
          }
        }
      
      
      
        async displayCoRelation() {
      
          this.deviceIdArr = (window.parent.parent.parent[7] as any).A_deviceIdArr;
          if (this.deviceIdArr == "null") {
            this.deviceIdArr = "";
          }
          this.coRelationId = (window.parent.parent.parent[7] as any).A_reportId;
          if (this.coRelationId == "null") {
            this.coRelationId = "";
          }
          if(this.DisplayCoRelationflag == 1){
            this.deviceCoordinatesArr = await this.datacrowdService.getAllCoRelationCommonLocationHits(this.coRelationId);
          }else{
          this.deviceCoordinatesArr =await this.datacrowdService.getCoRelationCommonLocationHits(this.coRelationId,this.deviceIdArr);
          }
      
        
      
          this.datajson.markerPositions=this.deviceCoordinatesArr;
          if (this.datajson.markerPositions !== null) {
           
    this.displayclusters=true;
    for (let i = 0; i < this.deviceCoordinatesArr.length; i++) {
      let point = new Point([
        parseFloat(this.deviceCoordinatesArr[i][1]),
        parseFloat(this.deviceCoordinatesArr[i][0]),
      ]);
      this.marker = new Feature({
        geometry: point,
        name: 'Marker',
      });
      this.marker.set('type', 'marker');
      this.marker.set('info', this.deviceCoordinatesArr[i]);
      this.clusterFeatures.push(this.marker);
    }

    this.source = new VectorSource({
      features: this.clusterFeatures,
    });
    this.clusterSource = new Cluster({
      source: this.source,
    });

    const styleCache: { [key: number]: Style } = {};

    this.clusters = new VectorLayer({
      source: this.clusterSource,
      style: (feature) => {
        const size = feature.get('features').length;
        let style = styleCache[size];
        if (!style) {
          if (size < 20) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#2EFF2E40',
                }),
                fill: new Fill({
                  color: '#2EFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 20 && size < 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FFFF2E40',
                }),
                fill: new Fill({
                  color: '#FFFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FF660040',
                }),
                fill: new Fill({
                  color: '#FF660090',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
          }
          styleCache[size] = style;
        }
        return style;
      },
    });
    this.clusters.set('type', 'marker');
    this.manysimularray.push(this.clusters);
    this.map.addLayer(this.clusters);
    if (this.reportType == 2) {
      // this.clustersZoom(datajs);
    }


    this.map.on('singleclick', (event) => {
      // get the feature you clicked
      const feature = this.map.forEachFeatureAtPixel(
        event.pixel,
        (feature: any) => {
          return feature;
        }
      );
    });
  
      
        }
      }

      
  getaoiactivity(bodyParam: any) {

    //   let response:any;
    let queryId: String;
    queryId = bodyParam.queryId;
    this.datacrowdService.distanceAPI(bodyParam).then(response => {
      response = response;
      console.log("responceee>>>", response);
    });

  }
  async displayAOIActivitytData() {
    this.beginOperation = (window.parent.parent.parent[7] as any).A_beginOperation;
    if (this.beginOperation == "null") {
      this.beginOperation = "";
    }
    this.endOperation = (window.parent.parent.parent[7] as any).A_endOperation;
    if (this.endOperation == "null") {
      this.endOperation = "";
    }

    this.deviceIdArr = (window.parent.parent.parent[7] as any).A_deviceIdArr;
    if (this.deviceIdArr == "null") {
      this.deviceIdArr = "";
    }


    if (
      (window.parent.parent.parent[7] as any) &&
      (window.parent.parent.parent[7] as any).A_COUNTRY_CODE
    ) {
      this.countrycode = (window.parent.parent.parent[7] as any).A_COUNTRY_CODE;
      console.log('countrycode:::::', this.countrycode);

      if (typeof this.countrycode !== 'undefined') {
        console.log('countrycode not empty>>>>');

          let numArr = this.countrycode.split(',').map(Number);
      numArr = this.getDistinctNumbers(numArr);
      console.log('numArr>>>>>>>', numArr);
      this.countrycode=numArr;
       await this.datacrowdService.getcountry2(this.countrycode).then((res:any)=>{
          console.log('getcountry2>>>>',res);
          this.countrycode=res;
          this.countrycode=this.convertCountryCode(this.countrycode);
        })
      }

    }else{
      if(this.reportType!=1 && this.reportType!=10 && this.reportType!=3 && this.reportType!=8 && this.reportType!=9 && this.reportType!=11){
        await this.datacrowdService.getALLcountryIDS().then(async (res:any)=>{
          console.log('getALLcountryIDS>>>>',res);
  
          this.countrycode=res;
  
           await this.datacrowdService.getcountry2(this.countrycode).then((res:any)=>{
              console.log('getcountry2>>>>',res);
              this.countrycode=res;
              this.countrycode=this.convertCountryCode(this.countrycode);
              console.log("countrycode finall",this.countrycode)
            })
  
        })
      }
     
    }


 


   
    if (typeof this.countrycode == 'undefined') {
      this.countrycode = '';
    }
    let queryjson = {
      reportName: "No Name",
      reportType: "2",
      reportTypeId: "2",
      TimeZone: this.TimeZone,
      RecipientUser: this.RecipientUser,
      DateTimeFrom: this.changeformatdate(this.beginOperation),
      RecipientEmail: this.RecipientEmail,
      DateTimeTo: this.changeformatdate(this.endOperation),
      Coordinates: [] as any[],
      meter: this.meter,
      Devices: this.deviceIdArr,
      isCSVAttached: this.isCSVAttached,
      dataType: this.dataType,
      telephoneNumber: this.telephoneNumber,
      EDGEHEIGHT: this.EDGEHEIGHT,
      simulationId: this.simulationid,
      userCode: this.usercode,
      imsiId: this.ImsiID,
      countryCode:[
        [
            "145",
            "142",
            [
                "376",
                "275",
                "634",
                "887",
                "196",
                "268",
                "368",
                "400",
                "414",
                "422",
                "512",
                "682",
                "760",
                "792",
                "784"
            ]
        ],
        [
            "35",
            "142",
            [
                "704",
                "116",
                "418",
                "360",
                "458",
                "104",
                "608",
                "702",
                "764",
                "626"
            ]
        ],
        [
            "61",
            "9",
            [
                "876",
                "184",
                "612",
                "876",
                "258",
                "570",
                "882",
                "772",
                "776",
                "798"
            ]
        ],
        [
            "419",
            "19",
            [
                "858",
                "531",
                "218",
                "254",
                "320",
                "328",
                "340",
                "558",
                "862",
                "850",
                "660",
                "533",
                "136",
                "152",
                "170",
                "188",
                "192",
                "212",
                "214",
                "222",
                "238",
                "308",
                "312",
                "332",
                "388",
                "474",
                "484",
                "500",
                "591",
                "600",
                "604",
                "630",
                "659",
                "662",
                "670",
                "652",
                "663",
                "239",
                "740",
                "780",
                "796"
            ]
        ],
        [
            "202",
            "2",
            [
                "204",
                "140",
                "232",
                "270",
                "430",
                "454",
                "175",
                "508",
                "894",
                "716",
                "854",
                "108",
                "120",
                "132",
                "148",
                "174",
                "178",
                "384",
                "180",
                "262",
                "226",
                "231",
                "260",
                "266",
                "288",
                "324",
                "624",
                "404",
                "426",
                "450",
                "466",
                "478",
                "480",
                "516",
                "562",
                "566",
                "638",
                "646",
                "654",
                "678",
                "686",
                "690",
                "694",
                "706",
                "710",
                "748",
                "834",
                "768",
                "800",
                "180"
            ]
        ],
        [
            "53",
            "9",
            [
                "166",
                "162",
                "334",
                "554",
                "574"
            ]
        ],
        [
            "154",
            "150",
            [
                "208",
                "833",
                "832",
                "248",
                "248",
                "233",
                "234",
                "246",
                "831",
                "352",
                "372",
                "428",
                "440",
                "578",
                "744",
                "752",
                "826"
            ]
        ],
        [
            "54",
            "9",
            [
                "242",
                "548",
                "540",
                "598"
            ]
        ],
        [
            "34",
            "142",
            [
                "356",
                "364",
                "462",
                "524",
                "586",
                "144"
            ]
        ],
        [
            "57",
            "9",
            [
                "296",
                "584",
                "580",
                "581",
                "316",
                "583",
                "520",
                "585"
            ]
        ],
        [
            "155",
            "150",
            [
                "442",
                "528",
                "250",
                "276",
                "438",
                "492",
                "756"
            ]
        ],
        [
            "30",
            "142",
            [
                "496",
                "156",
                "344",
                "392",
                "408",
                "410",
                "446",
                "158"
            ]
        ],
        [
            "21",
            "19",
            [
                "840",
                "124",
                "304",
                "666"
            ]
        ],
        [
            "143",
            "142",
            [
                "860",
                "398",
                "417",
                "762",
                "795"
            ]
        ],
        [
            "151",
            "150",
            [
                "112",
                "100",
                "203",
                "348",
                "498",
                "616",
                "642",
                "643",
                "703",
                "804"
            ]
        ],
        [
            "39",
            "150",
            [
                "191",
                "292",
                "300",
                "336",
                "380",
                "807",
                "470",
                "499",
                "620",
                "674",
                "688",
                "705",
                "724"
            ]
        ],
        [
            "15",
            "2",
            [
                "818",
                "434",
                "504",
                "788",
                "732"
            ]
        ]
    ],
    BtsTypeSlected:""

    };
    console.log('zz queryjson',queryjson)
    this.test(queryjson);
    this.displayAOIResultData();
  }

  async displayAOIResultData() {
    this.aoiId = (window.parent.parent.parent[7] as any).A_ReportId;
    if (this.aoiId == "null") {
      this.aoiId = "";
    }
    this.LNGArr = (window.parent.parent.parent[7] as any).A_LNGArr;
    if (this.LNGArr == "null") {
      this.LNGArr = "";
    }
    this.LATArr = (window.parent.parent.parent[7] as any).A_LATArr;
    if (this.LATArr == "null") {
      this.LATArr = "";
    }
    this.AoiResultCoordArr =await this.datacrowdService.getAoiCoord(this.aoiId,this.deviceIdArr);

    const body = { "LNGArr": this.LNGArr, "LATArr": this.LATArr, "AoiResultCoordArr": this.AoiResultCoordArr };

/////display markers
    this.AoiResultCoordArr.forEach((element2: any, key: any) => {
      let point = new Point([
        parseFloat(element2[1]),
        parseFloat(element2[0]),
      ]);
      this.markeraoi = new Feature({
        geometry: point,
        name: 'Marker',
      });
      this.markeraoi.set('type', 'marker');
      this.markeraoi.set('info', element2);
      this.clusterFeaturesaoi.push(this.markeraoi);
    });

    this.sourceaoi = new VectorSource({
      features: this.clusterFeaturesaoi,
    });
    this.clusterSourceaoi = new Cluster({
      source: this.sourceaoi,
    });

    const styleCache: { [key: number]: Style } = {};

    this.clustersaoi = new VectorLayer({
      source: this.clusterSourceaoi,
      style: (feature) => {
        const size = feature.get('features').length;
        let style = styleCache[size];
        if (!style) {
          if (size < 20) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#2EFF2E40',
                }),
                fill: new Fill({
                  color: '#2EFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 20 && size < 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FFFF2E40',
                }),
                fill: new Fill({
                  color: '#FFFF2E90',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
            styleCache[size] = style;
          } else if (size >= 100) {
            style = new Style({
              image: new CircleStyle({
                radius: 20,
                stroke: new Stroke({
                  width: 10,
                  color: '#FF660040',
                }),
                fill: new Fill({
                  color: '#FF660090',
                }),
              }),
              text: new Text({
                text: size.toString(),
                fill: new Fill({
                  color: '#000',
                }),
              }),
            });
          }
          styleCache[size] = style;
        }
        return style;
      },
    });
    this.clustersaoi.set('type', 'marker');
    this.manysimularray.push(this.clustersaoi);
    this.map.addLayer(this.clustersaoi);



    let splitLNGArr: any[];
    let splitLATArr: any[];

    splitLNGArr =this.LNGArr.split(',');
    splitLATArr = this.LATArr.split(',')
    /////display circles
    for (let i = 0; i < splitLNGArr.length; i++) {

      this.displayCircle(Number(splitLNGArr[i]),Number(splitLATArr[i]),this.AOICircleRadius,i);
      // this.circle = L.circle([Number(splitLATArr[i]), Number(splitLNGArr[i])], this.AOICircleRadius, {
      //   color: "#6e83f0",
      //   fillOpacity: 0.5,
      //   radius: this.AOICircleRadius,
      // });
      // this.layerGroup.addLayer(this.circle);

    }
    // this.layerGroup.addTo(this.map);
    // this.map.addLayer(this.marker);

  }

  executeAOIActivity() {
    (window.parent.parent.parent[8] as any).closepopup();
    this.aoiId = (window.parent.parent.parent[7] as any).A_reportId;
    if (this.reportId == "null") {
      this.reportId = "";
    }
    this.beginOperation = (window.parent.parent.parent[7] as any).A_beginOperation;
    if (this.beginOperation == "null") {
      this.beginOperation = "";
    }
    this.endOperation = (window.parent.parent.parent[7] as any).A_endOperation;
    if (this.endOperation == "null") {
      this.endOperation = "";
    }
    this.deviceIdArr = (window.parent.parent.parent[7] as any).A_deviceIdArr;
    if (this.deviceIdArr == "null") {
      this.deviceIdArr = "";
    }

    const body = { "beginOperation": this.beginOperation, "endOperation": this.endOperation, "deviceIdArr": this.deviceIdArr, "reportId": this.aoiId };
    let response = this.executeAOI(body);

  }
  async executeAOI(obj: any) {
    try {
      // this.notification('info','Processing AOI ');
      this.datacrowdService.executeAOIActivity(obj);
      //  this.loaderService.hide();
       this.checkAoiCounter = 0;
       //const dialogRef = this.dialog.open(this.CoTravelersReady);
       this.aoiSubscription = interval(1000).pipe(
         switchMap(() => this.datacrowdService.checkTableAoiActivity(this.aoiId, this.checkAoiCounter))
       )
         .subscribe(data => {
           this.checkAoiCounter++;
          //  this.loaderService.hide();
           if (data != 0) {
             this.aoiSubscription.unsubscribe();
            //  this.notification('success', 'AOI Is ready'); 
           }
         });
     } catch (error: any) {
      //  this.notification('error', 'AOI Failure');
       console.log("failure exception", error);
       console.log("Fail - Please contact valoores team");
     }
   }
  
  displayAOIPolygonData() {
    this.shapeIdArr = (window.parent.parent.parent[7] as any).A_shapeIdArr;
    console.log("shapeIdArr",this.shapeIdArr)
    console.log("shapeIdArr",this.shapeIdArr.split(','))
    this.shapeIdArr=this.shapeIdArr.split(',');

    if (this.shapeIdArr == "null") {
      this.shapeIdArr = "";
    }
    for (let i = 0; i < this.shapeIdArr.length; i++) {
      this.datacrowdService.getSelectedShape(this.shapeIdArr[i]).then((res) => {
        this.Aoiresp = res;
        this.coordinatesArray.push(this.Aoiresp);
        console.log("resssssssssssss----------",res);
        if (this.Aoiresp.Type == "Circle") {

          // this.circle = L.circle(this.Aoiresp.center, {
          //   color: "#FF0000",
          //   fillOpacity: 0.6,
          //   radius: this.Aoiresp.radius,
          // }).bindTooltip(`${this.Aoiresp.Name}`, {
          //   permanent: true,
          //   interactive: true,
          //   opacity: 0.6,
          //   direction: "center"
          // }).openTooltip();

          // this.layerGroup.addLayer(this.circle);

          // this.displayCircle()

        } else if (this.Aoiresp.Type == "Polygon") {

          // this.polygon = L.polygon(this.Aoiresp.Bounds, {
          //   color: "#FF0000  ",
          //   fillOpacity: 0.6,
          // }).bindTooltip(`${this.Aoiresp.Name}`, {
          //   permanent: true,
          //   interactive: true,
          //   opacity: 0.6,
          //   direction: "center"
          // })

          // this.layerGroup.addLayer(this.polygon);

        } else if (this.Aoiresp.Type == "Polyline") {
          // this.polyline = L.polyline(this.Aoiresp.Bounds, {
          //   color: "#FF0000  ",
          //   fillOpacity: 0.6,
          // }).bindTooltip(`${this.Aoiresp.Name}`, {
          //   permanent: true,
          //   interactive: true,
          //   opacity: 0.6,
          //   direction: "center"
          // })
          // this.layerGroup.addLayer(this.polyline);
        }

        if (this.AoiIds.length > 1) {
          // this.map.setView(this.Aoiresp.center, 5);
        }
        else {
          // this.map.setView(this.Aoiresp.center, 12);

        };

        // this.layerGroup.addTo(this.map);
      });


    }

  }

  changeformatdate(beginOperation:any){
    let dateee= beginOperation.split('-');
    let year=dateee[0]; 
    let month=dateee[1];
    let day=dateee[2];
    let res=month+'/'+day+'/'+year+' 00:00';
    return res;
  }

   
  async displayTimelineSimul(){
    let x:any=this.dataservice1.getTimelineSimulID();  
    this.usercode = (window.parent as any).AV2_userCode;
    if(x.toString().indexOf(',') != -1){
      const idsArray = x.split(',');
      for (const id of idsArray) {
        await this.datacrowdService
        .getsimualtion(id, this.usercode)
        .then((res) => {
          this.datajson = res;
        });
        this.displayClusters(this.datajson);
      }
     
      
    }else{
  
    
      await this.datacrowdService
      .getsimualtion(x, this.usercode)
      .then((res) => {
        this.datajson = res;
      });
      this.displayClusters(this.datajson);

    }
  }

 
  toggleDropdown() {
    var dropdownContent:any = document.getElementById("dropdownContent1");
    if (dropdownContent.style.display === "block") {
      dropdownContent.style.display = "none";
    } else {
      dropdownContent.style.display = "block";
    }
  }

  changebarRoute(){
    this.stopRoute();
    this.startRoute();
    console.log("speedTimeRoute----------",this.speedTimeRoute)
  }

  changebarRoute1(){
    this.stopRoute();
    this.startRoute1();
    console.log("speedTimeRoute----------",this.speedTimeRoute)
  }

  startRoute(){
    this.isRunningRoute = true;
    this.getrouteLine();
  }

  startRoute1(){
    this.isRunningRoute = true;
    this.displaybyroute();
  }


  stopRoute(){
  this.isRunningRoute = false;
  }

  async getrouteLine(){
    if (this.isRunningRoute==false) {
      return;
  }
    this.openTable=true;
    // this.isRunningRoute = true;
    console.log("this.isRunningRoute----",this.isRunningRoute);
    let array:any[]=[];
    let route:any[]=[];
    let routeLoop:any[]=[];
    let routeDevices1:any[]=[];
    let routeDevices:any[]=[];
  
    // this.routeDevices='4e79560f-e59a-4d7b-8b91-6dddbd571c57,436cab63-5002-475d-8d11-c321e5850659';
        
    routeDevices = this.dataservice1.getroutedevices().split(',');
    this.routeDevicestable=routeDevices;
    console.log("this.routeDevices-----------", routeDevices)
    let colorarray:any[]=['green','red','blue','yellow','purpule'];
     routeDevices.forEach((deviceId:any,index:any)=>{
       
      routeDevices1.push({'deviceid':deviceId,'color':colorarray[index]})
    });

  // let routeDevices1= [{'deviceid':'4e79560f-e59a-4d7b-8b91-6dddbd571c57','color':'green'},{'deviceid':'436cab63-5002-475d-8d11-c321e5850659','color':"red"}]
  this.displayedColumns = ['deviceid','Time','Lng', 'lat'];

   
    let datajson:any;
    // await this.http.get<any[]>('/assets/angularjson.json').subscribe((data:any[]) => {
      $('#controlbutton').css('display', '');
      $('#routebar').css('display', '');
      // console.log("data-----------",data)
      // datajson=data;
      datajson=this.datajson;
      // datajson=this.datajson;
       this.routedatajson=datajson;

       let markerPositions = datajson['markerPositions'];
       console.log("markerPositions-----------",markerPositions)
       this.routedatajson=this.routedatajson;
       this.displayRoute=true;
       this.displayclusters=true;
      // this.displayRouteLine(markerPositions);

      // console.log('routedatajson----------',this.routedatajson);


    let color;
    let x1:any=[{'deviceid':'','color':''}];
    let data11:any=this.routedatajson.markerPositions;
    // console.log("data11----",data11);
    // console.log("animatedmarker1----",this.animatedmarker1);
    // console.log("route[route.length-1]----",route[route.length-1]);
    let arrayfortable:any[]=[];
    
      console.log("speedTimeRoute1111111----",this.speedTimeRoute);
      // console.log("routeDevices1----", routeDevices1);
    // for( let i=this.indexRoute;i<data11.length;i++){
      // console.log("data11[i][2]----", data11[i][2]);
    
      setTimeout(async ()  => {
        let elt=data11[this.indexRoute];
        let elt1=data11[this.indexRoute+1];
        x1 =  routeDevices1.find((a:any)=>{
        // console.log("aaaaaa----",a);
        return  a.deviceid==elt[2]
      });

 color=x1.color;



var endMarker = new ol.Feature({geometry:  new Point([parseFloat(elt[1]),parseFloat(elt[0])]), color: color});
  
var routepoint = new VectorLayer({
  source: new VectorSource({
    features: [endMarker],
  }),
  style: [
    new Style({
      fill: new Fill({
        color: color,
      }),
      stroke: new Stroke({
        color: color,
        width: 3,
      }),
      image: new CircleStyle({
        radius: 7,
        fill: new Fill({
          color: color,
        }),
      }),
    }),
  ],
});

var routepointLoop = new VectorLayer({
  source: new VectorSource({
    features: [endMarker],
  }),
  style: [
    new Style({
      fill: new Fill({
        color: color,
      }),
      stroke: new Stroke({
        color: color,
        width: 3,
      }),
      image: new CircleStyle({
        radius: 7,
        fill: new Fill({
          color: color,
        }),
      }),
    }),
  ],
});


this.map.addLayer(routepoint);
this.magnifiedMap.addLayer(routepointLoop);

this.animatedmarker1=routepoint;
this.animatedmarker1Loop=routepointLoop;



this.animatedmarker1.deviceid=x1.deviceid;

if(this.routingMarkerArr[elt[2]]){
   this.map.removeLayer(this.routingMarkerArr[elt[2]]);
}
if(this.routingMarkerArrLoop[elt[2]]){
  this.magnifiedMap.removeLayer(this.routingMarkerArrLoop[elt[2]]);
}

this.routingMarkerArr[elt[2]]=this.animatedmarker1;
this.routingMarkerArrLoop[elt[2]]=this.animatedmarker1Loop;

route.push(this.animatedmarker1);
routeLoop.push(this.animatedmarker1Loop);

if(this.animatedmarker1!='undefined'){
  this.routeDeviceArray.push(this.animatedmarker1);
}
if(this.animatedmarker1Loop!='undefined'){
  this.routeDeviceArrayLoop.push(this.animatedmarker1Loop);
}
arrayfortable.push(elt);
 

this.typeofdata='devicehistory'
this.opentableData(arrayfortable,this.typeofdata);
this.indexRoute++;

if (this.isRunningRoute==false) {
return;
}

await this.getrouteLine();


}, 3000/this.speedTimeRoute);
 

// }
console.log("this.speedTimeRoute222222----",this.speedTimeRoute);
    // });
  }
  async displaybyroute(){
    let routeDevices1:any[]=[];
    let routeDevices:any[]=[];
    let objcolor:any[]=[];
    this,this.openTable=true;
    this.isRunningRoute=true;
    $('#routebar1').css('display', '');
    $('#routebar').css('display', 'none');
  //   if (this.isRunningRoute==false) {
  //     return;
  // }
    // this.routeDevices='4e79560f-e59a-4d7b-8b91-6dddbd571c57,436cab63-5002-475d-8d11-c321e5850659';
    
    routeDevices = this.dataservice1.getroutedevices().split(',');
    this.routeDevicestable=routeDevices;
    console.log("this.routeDevices-----------", routeDevices)
    let colorarray:any[]=['green','red','blue','yellow','purpule'];
    //random color
    // let color = `rgb(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)})`;
  
     routeDevices.forEach((deviceId:any,index:any)=>{
       
      routeDevices1.push({'deviceid':deviceId,'color':colorarray[index]});
      // routeDevices1.push({'deviceid':deviceId,'color':color});
    });
  
  // let routeDevices1= [{'deviceid':'4e79560f-e59a-4d7b-8b91-6dddbd571c57','color':'green'},{'deviceid':'436cab63-5002-475d-8d11-c321e5850659','color':"red"}]
  this.displayedColumns = ['deviceid','Time','Lng', 'lat'];
  
   
    let datajson:any;
    // await this.http.get<any[]>('/assets/angularjson.json').subscribe((data:any[]) => {
      // console.log("data-----------",data)
      // datajson=data;
      datajson=this.datajson;
      this.routedatajson=datajson;
  
      let markerPositions = datajson['markerPositions'];
      routeDevices1.forEach((a:any)=> { 
      let deviceArray=  markerPositions.filter((elt:any)=>{
          return  a.deviceid===elt[2]
            
          });
          objcolor.push({color:a.color,data:deviceArray,deviceId:a.deviceid,index:0});
        });
  
        this.displayPolylines(markerPositions,objcolor);
        // });
      }

      displayPolylines(markerPositions: any[],objcolor:any[]): void {
  
        if (this.isRunningRoute==false) {
        return;
    }
      const interval = setTimeout(async() => {
        if (this.currentIndex >= markerPositions.length) {
          clearInterval(interval); // Stop when all polylines are displayed
          return;
        }
    
        let currentDevice= markerPositions[this.currentIndex][2];
        console.log("currentDevice-----------",currentDevice);
    
      let x=  objcolor.find((elt:any)=>{
        return elt.deviceId==currentDevice});
        console.log("x-----------",x);
          // x.index=this.routecountobj;
        if(x.index+1>=x.data.length)
          {
        
            }
        else if(x.index<=x.data.length) {
          // this.polylines.push(polyline);
    
          // if(this.currentIndex>0){
       
          // let polyline = L.polyline([[x.data[x.index][0],x.data[x.index][1]],[x.data[x.index+1][0],x.data[x.index+1][1]]],{color:x.color}).addTo(this.map);
          // let color = `rgb(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)})`;
          
          // let color = 'red';
          const styles = [
            new Style({
              fill: new Fill({
                color: x.color,
              }),
              stroke: new Stroke({
                color: x.color,
                width: 3,
              }),
              image: new CircleStyle({
                radius: 7,
                fill: new Fill({
                  color: x.color,
                }),
              }),
            }),
          ];

          this.sourcePolyline = new VectorSource({
            features: [new Feature(new LineString([[x.data[x.index][1],x.data[x.index][0]],[x.data[x.index+1][1],x.data[x.index+1][0]]]))],
          });
          this.layerPolyline = new VectorLayer({
            source: this.sourcePolyline,
            style: styles,
          });
          this.polylineRouteArray.push(this.layerPolyline);
          this.map.addLayer(this.layerPolyline);
          // Decorate the polyline
          // L.polylineDecorator(polyline, {
          //   patterns: [{
          //     offset: 25,
          //     repeat: 300,
          //     symbol: L.Symbol.arrowHead({
          //       pixelSize: 15,
          //       pathOptions: {
          //         fillOpacity: 1,
          //         weight: 0
          //       }
          //     })
          //   }]
          // }).addTo(this.map);
         
          // this.polylineRouteArray.push(polyline);
          this.xroutearray.push(markerPositions[this.currentIndex]);
          this.typeofdata='devicehistory'
          this.opentableData(this.xroutearray,this.typeofdata);
    }
        
      // }
      
    
        this.currentIndex++;
        x.index++;
        await this.displayPolylines(markerPositions,objcolor);
      },  3000/this.speedTimeRoute); // Adjust the interval duration as needed
    } 

   
      clearRoutes(){
        this.routeDeviceArray.forEach((elt:any)=>{
          this.map.removeLayer(elt);
          });
        this.polylineRouteArray.forEach((elt:any)=>{
        this.map.removeLayer(elt);
        });
        this.routePointAnal.forEach((elt:any)=>{
          this.map.removeLayer(elt);
          });
        this.routePointAnalLoop.forEach((elt:any)=>{
            this.magnifiedMap.removeLayer(elt);
            });
        this.routeolylineAnal.forEach((elt:any)=>{
          this.map.removeLayer(elt);
          });
          this.routeolylineAnalLoop.forEach((elt:any)=>{
            this.magnifiedMap.removeLayer(elt);
            });
            this.fixedbtsGroupArrayLoop.forEach((elt:any)=>{
              this.magnifiedMap.removeLayer(elt);
              });
              this.FixedsectorarrayLoopLayer.forEach((elt:any)=>{
                this.magnifiedMap.removeLayer(elt);
                });
        this.isRunningRoute = false;
        this.openTable=false;
        $('#controlbutton').css('display', 'none');
        $('#routebar').css('display', 'none');
        $('#routebar1').css('display', 'none');
        
        this.routeDeviceArray=[];
        this.typeofdata='';
        this.speedTimeRoute=0;
        this.routingpolyline=[];
        this.routingicons=[];
        this.currentIndex=0;
        this.displayedColumns =[];
        this.Datatable1=[];
        this.Datatable=[];
        this.polylineRouteArray=[];
        this.tablerow=0;
        this.indexRoute=0;
        this.count=0;
        this.routeolylineAnal=[];
        this.routeolylineAnalLoop=[];
        this.routePointAnal=[];
        this.routePointAnalLoop=[];
        this.fixedbtsGroupArrayLoop=[];
        this.FixedsectorarrayLoopLayer=[];
        this.FixedsectorarrayLoop=[];
       
    
      }

      opentableData(array:any,typeofdata:any){
        this.openTable=true;
   
        if(typeofdata=='devicehistory'){
        
            // array2.push(array[i]); 
      
          array.forEach((elt:any)=>{
            let x:any = {
              deviceid: elt[2],
              Time: this.dateTtoDate(elt[3]),
              Lng: elt[1],
              lat: elt[0],
            }
            this.Datatable1.unshift(x);
            this.Datatable=this.Datatable1;
            this.tablerow++;
          })
        
         
   
        }
       else if(typeofdata=='trace')
       { 
        for (let i = this.count; i < array.length; i++) {
      
          // array2.push(array[i]);
  
          let x:any = {
           
            Time:  this.dateTtoDate(array[i].StartTime),
            StreetName: array[i].StreetName,
            Lng: array[i].NodeLongitude,
            lat: array[i].NodeLatitude,
          }
          this.Datatable1.unshift(x);
          this.Datatable=this.Datatable1;
          this.tablerow++;
        }
      }
        console.log("this.Datatable-----------",this.Datatable);
      }

      clickedDeviceId(eventData: any) {
        console.log('this.routedatajson------', this.routedatajson);
        console.log('eventData------', eventData);
        this.selectedDeviceId=eventData;
        console.log('row------', eventData.row);
        let routedatajson:any[]=this.routedatajson.markerPositions;
        let tableroutearray:any[]=[];
        console.log('this.routedatajson------', this.routedatajson);
  
         routedatajson.forEach((elt:any)=>{
         if(elt[2]==eventData.row){
           tableroutearray.push(elt);
         }
        });
        console.log("tableroutearray-----------",tableroutearray);
       
        this.opentableData(tableroutearray,this.typeofdata);
      }

 

      butnmap(){
    
        let obj22:any={
          senarioParentName:this.senarioParentName,
          simulationid:179701,
          Action:"addnewMenu",
          senariocount:this.senariocount
        }
          
        this.senarioIdOutput.emit(obj22);
          }

          // async getsenarioId(obj:any){
          //   console.log("obj><><><<>",obj);
          //   // this.Senario_reportType=obj.reportType;
        
          //   if(obj.action=="displaysenario"){
          //   this.dataservice1.setsenarioID(obj.simulID);
          //   // console.log('displaysenario>>>>>>>>>>>>',$("#displaysenario"));
          //   // $("#displaysenario").click();
          //   this.displaysenarioSequence();
          //   }else if(obj.action=="addnewSenario"){
          //     let parentSenario=localStorage.setItem("parentSenario",obj.simulID);
          //   this.senarioParentName=obj.simulID;
          //   this.addnewSenario()
          //     // this.mapChanges=obj;
                
          //     // let obj22:any={
          //     //   senarioParentName:this.senarioParentName,
          //     //   simulationid:this.simulationid,
          //     //   Action:"addnewMenu",
          //     //   senariocount:this.senariocount,
          //     //   senarioFlag:this.senarioFlag
              
          //     // }
                
          //     // // this.senarioIdOutput.emit(obj22);
          //     // this.navbarSimulId=obj22;
          //   }else if(obj.action=="addProperties"){

        
          //   }else  if(obj.action=="reset"){
          //     this.clearShapes();
        
          //   }else if(obj.action=="notSenario"){
          //     this.clearShapes();
              
          //   }
            
            
          //     }

          getsenarioId(obj:any){
            console.log("obj><><><<>",obj);
            // this.Senario_reportType=obj.reportType;
        
            if(obj.action=="displaysenario"){
            this.dataservice1.setsenarioID(obj.simulID);
            // console.log('displaysenario>>>>>>>>>>>>',$("#displaysenario"));
            // $("#displaysenario").click();
            this.displaysenarioSequence();
            }else if(obj.action=="addnewSenario"){
              let parentSenario=localStorage.setItem("parentSenario",obj.simulID);
            this.senarioParentName=obj.simulID;
            this.addnewSenario()
              // this.mapChanges=obj;
                
              // let obj22:any={
              //   senarioParentName:this.senarioParentName,
              //   simulationid:this.simulationid,
              //   Action:"addnewMenu",
              //   senariocount:this.senariocount,
              //   senarioFlag:this.senarioFlag
              
              // }
                
              // // this.senarioIdOutput.emit(obj22);
              // this.navbarSimulId=obj22;
            }else if(obj.action=="addProperties"){
        // this.modalService.open(this.SimulInfo);
        
        $(".modal-content").css("width", "650px");
        $(".modal-content").css("right", "200px");
        $(".modal-content").css("padding", "10px");
        $(".modal-content").css("top", "85px");
        $(".modal-content").draggable({
          axis: "both",
          cursor: "move"
        });
        
            }else  if(obj.action=="reset"){
              this.clearShapes();
        
            }else if(obj.action=="nextAction"){
        
              // this.showTextMenu=!this.showTextMenu;
              this.bars();
        
            }else if(obj.action=="refresh"){
              this.test33();
        
            }else if(obj.action=="ShowHeader"){
              this.ShowHeader=!this.ShowHeader;
              this.showTextMenu=false;
          
            }
            
              }

              displaysenarioSequence(){

                this.displaysenario(this.dataservice1.getsenarioID());
              }

              
              looplens(event:any){
                

        event.stopPropagation(); // Stop the event from propagating to the map
        event.preventDefault(); // Prevent the default button click behavior
        let  mapContainer: any = document.querySelector('.map');
         // let mapContainer = document.getElementById('map');
        this.isDimmed = true;
        const magnifyingGlass = document.getElementById('magnifying-glass1');

        // const elements = this.el.nativeElement.querySelectorAll('.leaflet-bar');


        this.openMagnifier=!this.openMagnifier;

        if(this.openMagnifier==true){
         $('.magnifying-glass1').css('display','none');
        // $('.magnifying-glass1').css('display','');
        mapContainer.classList.toggle('custom-cursor');
        // // Disable map interactions
        
        // $('.leaflet-bar').css('opacity','0.5');
        // $('.leaflet-bar').css('pointer-events','none');
        // $('#graphtools').css('opacity','0.5');
        // $('#graphtools').css('pointer-events','none');
        // $('.moretools').css('opacity','0.5');
        // $('.moretools').css('pointer-events','none');
  
        // this.map.dragging.disable();
        // this.map.scrollWheelZoom.disable();

           // Disable mouse wheel zoom
    this.map.getInteractions().forEach( (interaction)=> {
      if (interaction instanceof MouseWheelZoom) {
        interaction.setActive(false);
      }
    });

    // Disable drag pan
    this.map.getInteractions().forEach(function(interaction) {
      if (interaction instanceof DragAndDrop) {
        interaction.setActive(false);
      }
    });


        this.updateMagnifyingGlassSize();
          
        }else{
          mapContainer.classList.toggle('custom-cursor');
        //   $('.magnifying-glass1').css('display','none')
        //   // Enable map interactions
        // $('.leaflet-bar').css('opacity','1');
        // $('.leaflet-bar').css('pointer-events','');
        // $('#graphtools').css('opacity','1');
        // $('#graphtools').css('pointer-events','');
        // $('.moretools').css('opacity','1');
        // $('.moretools').css('pointer-events','');
        //   this.map.dragging.enable();
        //   this.map.scrollWheelZoom.enable();
        this.map.getInteractions().forEach( (interaction)=> {
          if (interaction instanceof MouseWheelZoom) {
            interaction.setActive(true);
          }
        });
    
        // Disable drag pan
        this.map.getInteractions().forEach(function(interaction) {
          if (interaction instanceof DragAndDrop) {
            interaction.setActive(true);
          }
        });

        
        }
              }

              @HostListener('document:keydown', ['$event'])
              handlekeybordEvent(event: KeyboardEvent) {
                  const magnifyingGlass:any = document.getElementById('magnifying-glass1');
              
                  if (event.ctrlKey && event.key === 'ArrowUp') {
                      if(this.openMagnifier) {
                          const currentWidth = parseFloat(window.getComputedStyle(magnifyingGlass).width);
                          const currentHeight = parseFloat(window.getComputedStyle(magnifyingGlass).height);
                        console.log("currentWidth----",currentWidth,"---currentHeight---",currentHeight)
                          magnifyingGlass.style.width = `${currentWidth + 50}px`;
                          magnifyingGlass.style.height = `${currentHeight + 50}px`;
                          this.updateMagnifyingGlassSize();
                      }
                  } else if (event.ctrlKey && event.key === 'ArrowDown') {
                      if(this.openMagnifier) {
                          const currentWidth = parseFloat(window.getComputedStyle(magnifyingGlass).width);
                          const currentHeight = parseFloat(window.getComputedStyle(magnifyingGlass).height);
              
                          magnifyingGlass.style.width = `${currentWidth - 50}px`;
                          magnifyingGlass.style.height = `${currentHeight - 50}px`;
                          this.updateMagnifyingGlassSize();
                      }
                  } else if (event.ctrlKey && event.key === 'Delete') {
                      if(this.openMagnifier) {
      
                          const mapContainer :any= document.getElementById('mapContainer');
                          // this.isDimmed = false;
                          $('.leaflet-bar').css('opacity', '1');
                          $('.leaflet-bar').css('pointer-events', '');
                          $('#graphtools').css('opacity','1');
                          $('#graphtools').css('pointer-events','');
                          $('.moretools').css('opacity','1');
                          $('.moretools').css('pointer-events','');

                          $('.magnifying-glass1').css('display', 'none');
                          this.openMagnifier = false;
                          this.isDimmed=false;
                    
                          this.map.getInteractions().forEach( (interaction)=> {
                            if (interaction instanceof MouseWheelZoom) {
                              interaction.setActive(true);
                            }
                          });
                      
                          // Disable drag pan
                          this.map.getInteractions().forEach(function(interaction) {
                            if (interaction instanceof DragAndDrop) {
                              interaction.setActive(true);
                            }
                          });

                          mapContainer.classList.remove('custom-cursor');
                      }
                  }
              }
              @HostListener('document:wheel', ['$event'])
              handleMouseWheelEvent(event: WheelEvent) {
                // if (event.ctrlKey) {
                   
                  if (event.deltaY > 0) {
                    // Scrolling down
                    if(this.openMagnifier==true){
                    this.zoommagnifymap=this.zoommagnifymap-1;
           
                    
                    this.view = new ol.View({
                      center: this.cursorlnglat,
                      zoom: this.zoommagnifymap,
                    });

                    this.magnifiedMap.setView(this.view);

                    //  this.magnifiedMap.setView( this.cursorlnglat,this.zoommagnifymap);
                    }
                  } else if (event.deltaY < 0) {
                    // Scrolling up
                    if(this.openMagnifier==true){
                      this.zoommagnifymap=this.zoommagnifymap+1;
                      
                      this.view = new ol.View({
                        center: this.cursorlnglat,
                        zoom: this.zoommagnifymap,
                      });
  
                      this.magnifiedMap.setView(this.view);
                      }
                  // }
                }
          
          
              }
            
       

              // updateMagnifyingGlass(event: any): void {
              //   console.log('event=------',event);
              //   let magnifyingGlass: any = document.getElementById('magnifying-glass1');
              //   let mapContainer: any = document.querySelector('.map');
              //   console.log('mapContainer=------',mapContainer);
              //   console.log('magnifyingGlass=------',magnifyingGlass);
                
              //   let mouseX = event.clientX - mapContainer.getBoundingClientRect().left;
              //   let mouseY = event.clientY - mapContainer.getBoundingClientRect().top;
              //   console.log('mouseX=------',mouseX);
              //   console.log('mouseY=------',mouseY);
                
              //   magnifyingGlass.style.left = mouseX - magnifyingGlass.offsetWidth / 2 + 'px';
              //   magnifyingGlass.style.top = mouseY - magnifyingGlass.offsetHeight / 2 + 'px';
            
              //   let mapCoordinate = this.map.getCoordinateFromPixel([mouseX, mouseY]);
              //   console.log('mapCoordinate=------',mapCoordinate);

              //   this.magnifiedMap.getView().setCenter(mapCoordinate);
            
              //   let bgPosX = (-mouseX * 2) + magnifyingGlass.offsetWidth / 2 + 'px';
              //   let bgPosY = (-mouseY * 2) + magnifyingGlass.offsetHeight / 2 + 'px';
              //   magnifyingGlass.style.backgroundPosition = bgPosX + ' ' + bgPosY;
              // }

              updateMagnifyingGlass(event: any): void {
                console.log('event=------', event);
                let magnifyingGlass: any = document.getElementById('magnifying-glass1');
                let mapContainer: any = document.querySelector('.map');
     
            
                // Extract pixel coordinates from the MapBrowserEvent
                let mouseX = event.pixel_[0];
                let mouseY = event.pixel_[1];
           
            
                magnifyingGlass.style.left = mouseX - magnifyingGlass.offsetWidth / 2 + 'px';
                magnifyingGlass.style.top = mouseY - magnifyingGlass.offsetHeight / 2 + 'px';
            
                // Assuming this.map is your OpenLayers Map instance
                let mapCoordinate = this.map.getCoordinateFromPixel([mouseX, mouseY]);
           
            
                this.magnifiedMap.getView().setCenter(mapCoordinate);
            
                let bgPosX = (-mouseX * 2) + magnifyingGlass.offsetWidth / 2 + 'px';
                let bgPosY = (-mouseY * 2) + magnifyingGlass.offsetHeight / 2 + 'px';
                magnifyingGlass.style.backgroundPosition = bgPosX + ' ' + bgPosY;
            }
            
              updateMagnifyingGlassSize() {
                let magnifyingGlass :any= document.getElementById('magnifying-glass1');
                let currentWidth = parseFloat(window.getComputedStyle(magnifyingGlass).width);
                let currentHeight = parseFloat(window.getComputedStyle(magnifyingGlass).height);
            
                // this.magnifiedMap.invalidateSize();
                // const center = this.magnifiedMap.getCenter();
                // this.magnifiedMap.setView(center);
                magnifyingGlass.style.backgroundSize = `${currentWidth * 4}px ${currentHeight * 4}px`;
            }
    
            
            onMouseOver( ) {
          
      
              if(this.openMagnifier==true ){
          
              //   // const mapContainer = document.getElementById('map');
                // this.isDimmed = false;
              //   // $('.leaflet-bar').css('opacity', '1');
              //   // $('.leaflet-bar').css('pointer-events', '');
    
                $('.magnifying-glass1').css('display', 'none');
              //   // this.openMagnifier = false;
              //   // this.map.dragging.enable();
              //   // this.map.scrollWheelZoom.enable();
              //   // mapContainer.classList.remove('custom-cursor');
            
              }
            }

            async hi(){
              this.usercode="8158";
          
            //  this.displayCircle(35,33,2000,123)
              this.displayClusters2("184545");
            }
            async getallroute1(){
              this.routeDevices = this.dataservice1.getroutedevices().split(',');
                console.log("this.routeDevices----",this.routeDevices);
            
             let DateTimeFrom=this.datePipe.transform(this.DateTimeFrom, 'yyyy-MM-dd');
             let DateTimeTo=this.datePipe.transform(this.DateTimeTo, 'yyyy-MM-dd');
             console.log("this.DateTimeTo----", DateTimeTo);
             console.log("this.DateTimeFrom----",DateTimeFrom);
                console.log("this.simulationid----",this.simulationid);
             
            
                // console.log("split----",this.routeDevices.split(','));
                let array:any=this.routeDevices;
                  // let array:any=this.Devices ;
                let obj:any={"end_date":DateTimeTo,"server":"10.1.10.110","device_id":array,"start_date":DateTimeFrom, "simulation_id":this.simulationid};
                console.log("obj----",obj);
                this.datacrowdService.getRoutes(obj).then((data:any)=>{
                  $('#controlbutton').css('display', '');
                  console.log("data-----------",data)
                   this.routedatajson=data;
                   this.displayRoute=true;
                   this.displayclusters=true;
                   this.displayallroute1(array);
            
                  console.log('routedatajson----------',this.routedatajson);
                  console.log('to millisecond----',new Date(this.routedatajson[0].StartTime).getTime());
            
                  this.routedatajson.sort((a:any, b:any) => new Date(a.StartTime).getTime() - new Date(b.StartTime).getTime());
                  console.log('routedatajson1111----------',this.routedatajson);
                });
            // this.routeDevices=['26e170d1-9bbc-4b1f-b3e9-4a7fd265a033','4e320097-5103-40eb-978e-fc079e1abba3']
            //     await this.httpClient.get<any[]>('/assets/routejson.json').subscribe(data => {
            //       $('#controlbutton').css('display', '');
            //       console.log("data-----------",data)
            //        this.routedatajson=data;
            //        this.displayRoute=true;
            //        this.displayclusters=true;
            //        this.displayallroute1();
            
            //       console.log('routedatajson----------',this.routedatajson);
            //       console.log('to millisecond----',new Date(this.routedatajson[0].StartTime).getTime());
            
            //       this.routedatajson.sort((a:any, b:any) => new Date(a.StartTime).getTime() - new Date(b.StartTime).getTime());
            //       console.log('routedatajson1111----------',this.routedatajson);
            //     });
            
             
              }

              displayallroute1(array:any){
                // displayallroute1(){
               
                  // this.routeDevices= ['26e170d1-9bbc-4b1f-b3e9-4a7fd265a033','4e320097-5103-40eb-978e-fc079e1abba3']
                  // this.routeDevices=['26e170d1-9bbc-4b1f-b3e9-4a7fd265a033','4e79560f-e59a-4d7b-8b91-6dddbd571c57'];
                  // let array:any[]=this.routeDevices
                  this.displayedColumns = ['Time', 'StreetName', 'Lng', 'lat'];
                  let tableroutearray:any[]=[];
                  let count=0;
                  array.forEach((deviceId:any,deviceindex:any)=>{
              
                    // let color = `rgb(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)})`;
                   let color:any[]=['red','blue'];
                    tableroutearray=[];
                    let array:any[]=[];
                    let routearray:any[]=[];
                 
                   this.routedatajson.forEach((elt:any)=>{
              
                    if(deviceId===array[0]){
                   //by default fist deviceId
              
                      if(elt.device_id===deviceId){
                      tableroutearray.push(elt);
                      }
                    }
                      if(elt.device_id===deviceId){
                        array.push(elt);
                        if(deviceindex>0){
                          routearray.push([elt.NodeLatitude,(parseFloat(elt.NodeLongitude)+0.000001).toString()]);
              
                        }else{
              
                        routearray.push([elt.NodeLatitude,(parseFloat(elt.NodeLongitude)).toString()]);
                      }
                      }
                     });
                    
                     console.log("array-----------",array);
                     console.log("routearray-----------",routearray)
                      
                

                    let  coordinates = routearray.map(coord => ([parseFloat(coord[1]), (coord[0])]));
                    console.log("coordinates11111-----------",coordinates);
                    // Create a LineString feature
                    let  lineString = new LineString(coordinates);
                
                    // Create a feature with the LineString geometry
                    let  feature = new Feature({
                      geometry: lineString
                    });
                
                    // Create a vector source and layer
                    let  vectorSource = new VectorSource({
                      features: [feature]
                    });
                
                    let  vectorLayer = new VectorLayer({
                      source: vectorSource,
                      style: new Style({
                        stroke: new Stroke({
                          color: color[count],
                          width: 2
                        })
                      })
                    });

                    let  vectorLayerLoop = new VectorLayer({
                      source: vectorSource,
                      style: new Style({
                        stroke: new Stroke({
                          color: color[count],
                          width: 2
                        })
                      })
                    });
                
                    // Add the vector layer to the map
                    this.map.addLayer(vectorLayer);
                    this.magnifiedMap.addLayer(vectorLayerLoop);
                    this.routeolylineAnal.push(vectorLayer);
                    this.routeolylineAnalLoop.push(vectorLayerLoop);

                    // Add the same vector layer to the magnified map
                   
                     count++;
                     var firstMarker = new ol.Feature({geometry:  new Point([routearray[0][1],parseFloat(routearray[0][0])]), color: color});
                     console.log("firstMarker",firstMarker);
                     var routepoint = new VectorLayer({
                      source: new VectorSource({
                        features: [firstMarker],
                      }),
                      style: [
                        new Style({
                          fill: new Fill({
                            color: color[0],
                          }),
                          stroke: new Stroke({
                            color: color[0],
                            width: 3,
                          }),
                          image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                              color: color[0],
                            }),
                          }),
                        }),
                      ],
                    });   

                    var routepointLoop = new VectorLayer({
                      source: new VectorSource({
                        features: [firstMarker],
                      }),
                      style: [
                        new Style({
                          fill: new Fill({
                            color: color[0],
                          }),
                          stroke: new Stroke({
                            color: color[0],
                            width: 3,
                          }),
                          image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                              color: color[0],
                            }),
                          }),
                        }),
                      ],
                    });   
               
                    this.map.addLayer(routepoint);
                    this.magnifiedMap.addLayer(routepointLoop);
                    this.routePointAnal.push(routepoint);
                    this.routePointAnalLoop.push(routepointLoop);

                    var endMarker = new ol.Feature({geometry:  new Point([routearray[routearray.length-1][1],parseFloat(routearray[routearray.length-1][0])]), color: color});
                    console.log("endMarker",endMarker);
                 
                    var routepointend = new VectorLayer({
                     source: new VectorSource({
                       features: [endMarker],
                     }),
                     style: [
                       new Style({
                         fill: new Fill({
                           color: color[1],
                         }),
                         stroke: new Stroke({
                           color: color[1],
                           width: 3,
                         }),
                         image: new CircleStyle({
                           radius: 7,
                           fill: new Fill({
                             color: color[1],
                           }),
                         }),
                       }),
                     ],
                   });   

                   var routepointendLoop = new VectorLayer({
                    source: new VectorSource({
                      features: [endMarker],
                    }),
                    style: [
                      new Style({
                        fill: new Fill({
                          color: color[1],
                        }),
                        stroke: new Stroke({
                          color: color[1],
                          width: 3,
                        }),
                        image: new CircleStyle({
                          radius: 7,
                          fill: new Fill({
                            color: color[1],
                          }),
                        }),
                      }),
                    ],
                  });   
                   console.log("routepointend===",routepointend);

                   this.map.addLayer(routepointend);
                   this.magnifiedMap.addLayer(routepointendLoop);
                   this.routePointAnal.push(routepointend);
                   this.routePointAnalLoop.push(routepointendLoop);
                  
                     console.log("tableroutearray-----------",tableroutearray);
                     console.log("routingicons-----------",this.routingicons);
                     if(tableroutearray.length!=0){
                      this.typeofdata='trace';
                      // this.opentableData(tableroutearray,this.typeofdata);
                     }
                    
                  });
              
                  
                
                }
             
  increaseSpeed() {
    if (this.speedTimeRoute < 100) {
      this.speedTimeRoute++;
      this.changebarRoute();
    }
  }

  decreaseSpeed() {
    if (this.speedTimeRoute > 1) {
      this.speedTimeRoute--;
      this.changebarRoute();
    }
  }
 
  increaseSpeed1() {
    if (this.speedTimeRoute < 100) {
      this.speedTimeRoute++;
      this.changebarRoute1();
    }
  }

  decreaseSpeed1() {
    if (this.speedTimeRoute > 1) {
      this.speedTimeRoute--;
      this.changebarRoute1();
    }
  }
  bydevice(){
    this.speedTimeRoute=0;
    this.increaseSpeed();
  }

  byroute(){
    this.speedTimeRoute=0;
    this.increaseSpeed1();
  }

  resetbyDevice(){
    this.clearRoutes();
    this.speedTimeRoute=1;
    $('#routebar').css('display', '');
 
  }

  resetbyRoute(){
    this.clearRoutes();
    this.speedTimeRoute=1;
    $('#routebar1').css('display', '');
 
  }
 

  async test33(){
   
    console.log("deviceValue",this.deviceValue) 
    console.log("selectedType",this.selectedType) 
    
    
    
    await this.datacrowdService.getSimulationId().then((res:any)=>{
      //console.log('getcountry2>>>>',res);
      this.simulationid=res;
      //console.log("countrycode finall",this.countrycode)
    })
    
    if(this.senariocount==0){
      this.senarioParentName=this.simulationid;
      this.firstsenario=this.simulationid;
      this.internalcode=this.simulationid;
    
    }
    if(this.reportType!=1 && this.reportType!=10 && this.reportType!=3 && this.reportType!=8 && this.reportType!=9 && this.reportType!=11){
      console.log('countrycode>>>>',this.countrycode);
        
        if(typeof this.countrycode=="undefined"){
    
        }
        await this.datacrowdService.getALLcountryIDS().then(async (res:any)=>{
          //console.log('getALLcountryIDS>>>>',res);
    
          this.countrycode=res;
    
           await this.datacrowdService.getcountry2(this.countrycode).then((res:any)=>{
              //console.log('getcountry2>>>>',res);
              this.countrycode=res;
              this.countrycode=this.convertCountryCode(this.countrycode);
              console.log("countrycode finall",this.countrycode)
            })
    
        })
      }
     
    
      let queryjson:any;
      this.reportType=this.selectedType;
      await console.log('reportType >>', this.reportType);
      if (typeof this.DateTimeFrom === 'undefined' || this.DateTimeFrom === null ||this.DateTimeFrom.includes('undefined')) {
        // Set DateTimeFrom to one year ago from today
        let oneYearAgo = new Date();
        oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
        this.DateTimeFrom = this.convertDate2(oneYearAgo.toISOString()); // Convert to ISO string format
    }
    
    // Check if DateTimeTo is undefined or null
    if (typeof this.DateTimeTo === 'undefined' || this.DateTimeTo === null || this.DateTimeTo.includes('undefined')) {
        // Set DateTimeTo to today's date
        let today = new Date();
        this.DateTimeTo = this.convertDate2(today.toISOString()); // Convert to ISO string format
    }
    
    if(this.reportType=="1" ){
    
      queryjson={
        "reportName": "No Name",
        "reportType": this.selectedType.toString(),
        "reportTypeId": this.selectedType.toString(),
        "TimeZone": "",
        "RecipientUser": "",
        "DateTimeFrom":  this.DateTimeFrom,
        "RecipientEmail": "",
        "DateTimeTo": this.DateTimeTo,
        "Coordinates":this.coordinatesArray,
        "meter": "",
        "Devices": this.deviceValue,
        "isCSVAttached": "",
        "dataType": "",
        "telephoneNumber": "",
        "EDGEHEIGHT": "10",
        "simulationId": this.simulationid.toString(),
        "userCode": "8158 ",
        "imsiId": this.IMSI_IDValue,
        "countryCode": "",
        "senario": "-1",
        "BtsTypeSlected": ""
      };
    }
    else if(this.reportType=="2"){
      // this.clearShapes();
    
   
    
      if (!JSON.parse(localStorage.getItem("multiselection"))) {
        this.multiselection = [];
    
      } else {
        //console.log('multiselection>>>>', JSON.parse(localStorage.getItem("multiselection")));
        //console.log('multiselection>>>>', JSON.parse(localStorage.getItem("multiselection")).join());
    
        this.multiselection = JSON.parse(localStorage.getItem("multiselection")).join();
        if (this.Devices == "" || typeof this.Devices == "undefined") {
    
          this.Devices = this.multiselection;
        } else {
    
          this.Devices = this.Devices + ',' + this.multiselection;
    
        }
      }
      
       queryjson={
        "reportName": "No Name",
        "reportType": this.selectedType.toString(),
        "reportTypeId": this.selectedType.toString(),
        "TimeZone": "",
        "RecipientUser": "",
        "DateTimeFrom":this.DateTimeFrom,
        "RecipientEmail": "",
        "DateTimeTo": this.DateTimeTo,
        "Coordinates":[],
        "meter": "",
        "Devices": this.Devices,
        "isCSVAttached": "",
        "dataType": "",
        "telephoneNumber": "",
        "EDGEHEIGHT": "10",
        "simulationId": this.simulationid.toString(),
        "userCode": "8158 ",
        "imsiId": this.IMSI_IDValue,
        "countryCode": this.countrycode,
        "senario": "-1",
        "BtsTypeSlected": ""
      };
    
    }
    
    if(this.reportType=="1" ||this.reportType=="6"){
    
      queryjson={
        "reportName": "No Name",
        "reportType": this.selectedType.toString(),
        "reportTypeId": this.selectedType.toString(),
        "TimeZone": "",
        "RecipientUser": "",
        "DateTimeFrom":  this.DateTimeFrom,
        "RecipientEmail": "",
        "DateTimeTo": this.DateTimeTo,
        "Coordinates":this.coordinatesArray,
        "meter": "",
        "Devices": this.deviceValue,
        "isCSVAttached": "",
        "dataType": "",
        "telephoneNumber": "",
        "EDGEHEIGHT": "10",
        "simulationId": this.simulationid.toString(),
        "userCode": "8158 ",
        "imsiId": this.IMSI_IDValue,
        "countryCode": "",
        "senario": "-1",
        "BtsTypeSlected": ""
      };
    }
    
    
    else if(this.reportType=="3"){
    
      if (!JSON.parse(localStorage.getItem("multiselection"))) {
        this.multiselection = [];
    
      } else {
        //console.log('multiselection>>>>', JSON.parse(localStorage.getItem("multiselection")));
        //console.log('multiselection>>>>', JSON.parse(localStorage.getItem("multiselection")).join());
    
        this.multiselection = JSON.parse(localStorage.getItem("multiselection")).join();
        if (this.Devices == "" || typeof this.Devices == "undefined") {
    
          this.Devices = this.multiselection;
        } else {
    
          this.Devices = this.Devices + ',' + this.multiselection;
    
        }
      }
         queryjson={
        "reportName": "No Name",
        "reportType": this.selectedType.toString(),
        "reportTypeId": this.selectedType.toString(),
        "TimeZone": "",
        "RecipientUser": "",
        "DateTimeFrom":  this.DateTimeFrom,
        "RecipientEmail": "",
        "DateTimeTo": this.DateTimeTo,
        "Coordinates":this.coordinatesArray,
        "meter": "",
        "Devices": this.Devices,
        "isCSVAttached": "",
        "dataType": "",
        "telephoneNumber": "",
        "EDGEHEIGHT": "10",
        "simulationId": this.simulationid.toString(),
        "userCode": "8158 ",
        "imsiId": this.IMSI_IDValue,
        "countryCode": "",
        "senario": "-1",
        "BtsTypeSlected": ""
      };
    
      
    }else if(this.reportType=="10"){
      //let device = localStorage.getItem('deviceselected');
     
      queryjson={
        "reportName": "No Name",
        "reportType": this.selectedType.toString(),
        "reportTypeId": this.selectedType.toString(),
        "TimeZone": "",
        "RecipientUser": "",
        "DateTimeFrom":  this.DateTimeFrom,
        "RecipientEmail": "",
        "DateTimeTo": this.DateTimeTo,
        "Coordinates":this.coordinatesArray,
        "meter": "",
        "Devices": this.deviceValue,
        "isCSVAttached": "",
        "dataType": "",
        "telephoneNumber": "",
        "EDGEHEIGHT": "10",
        "simulationId": this.simulationid.toString(),
        "userCode": "8158 ",
        "imsiId": this.IMSI_IDValue,
        "countryCode": "",
        "senario": "-1",
        "BtsTypeSlected": ""
      };
       
          
      console.log("queryjson",queryjson)
      this.datajson = await this.getSimulationData(queryjson);
      console.log('datajson>', this.datajson);
      this.scandevices();
    
    
    }else if(this.reportType=="11"){
      //let device = localStorage.getItem('deviceselected');
      queryjson={
        "reportName": "No Name",
        "reportType": this.selectedType.toString(),
        "reportTypeId": this.selectedType.toString(),
        "TimeZone": "",
        "RecipientUser": "",
        "DateTimeFrom": this.DateTimeFrom,
        "RecipientEmail": "",
        "DateTimeTo":this.DateTimeTo,
        "Coordinates":[],
        "meter": "",
        "Devices":"",
        "isCSVAttached": "",
        "dataType": "",
        "telephoneNumber": "",
        "EDGEHEIGHT": "10",
        "simulationId": this.simulationid.toString(),
        "userCode": "8158 ",
        "imsiId": this.ImsiID,
        "countryCode": "",
        "senario": "-1",
        "BtsTypeSlected": ""
      };
      await this.datacrowdService.getTCDSimulationObject(this.ImsiID).then((res:any)=>{
        console.log("getTCDSimulationObject res",res);
        this.datajson=res;
        this.tcd();
    
      })
    }
    
    else if(this.reportType=="8" ||this.reportType=="9"){
     
      // const dialogRef =  this.dialog.open(this.BtsType);
      //  //console.log("cRdaius111>>",$('.bulkRadius'))
    
    
    
      //  dialogRef.afterClosed().subscribe(async result => {
    
        console.log("BtsTypeSlected>>>>",this.BtsTypeSlected)
        queryjson={
          "reportName": "No Name",
          "reportType": this.selectedType.toString(),
          "reportTypeId": this.selectedType.toString(),
          "TimeZone": "",
          "RecipientUser": "",
          "DateTimeFrom":  this.DateTimeFrom,
          "RecipientEmail": "",
          "DateTimeTo": this.DateTimeTo,
          "Coordinates":this.coordinatesArray,
          "meter": "",
          "Devices": this.deviceValue,
          "isCSVAttached": "",
          "dataType": "",
          "telephoneNumber": "",
          "EDGEHEIGHT": "10",
          "simulationId": this.simulationid.toString(),
          "userCode": "8158 ",
          "imsiId": this.IMSI_IDValue,
          "countryCode": "",
          "senario": "-1",
          "BtsTypeSlected": this.BtsTypeSlected
        };
    
    
        console.log('queryjson >>', queryjson);
    
       
            
      this.datajson = await this.getSimulationData(queryjson);
      console.log('datajson>', this.datajson);
      console.log('this.BtsTypeSlected>', this.BtsTypeSlected);
      if(this.BtsTypeSlected=="BTS"){
    
      }else{
       
        this.displayFixedElements(this.datajson.fixedelements);
        this.displayClusters(this.datajson.geo);
         
      }
      
    
    
      // });
    
    }
    
    
    
    console.log("this.reportType  >>",this.reportType)
    console.log("this.reportType  >>",typeof this.reportType)
    
    
        console.log("queryjson IIII  ",queryjson)
    if(this.reportType !="11" && this.reportType!="8" && this.reportType!="9" && this.reportType!="10" ){
        console.log('queryjson >>', queryjson);
    
    
    
        
      //   for (const elt of queryjson.Coordinates) {
      //     if(elt.Type==='Circle'){
      //       let turfshape:any = turf.circle([elt.center.lng, elt.center.lat], elt.radius / 1000, { units: 'kilometers' });
    
      //       const intersectingRegions:any = [];
       
      //       //console.log("geoJsonData ",this.geojsonData);
      //       // Iterate through each feature in the GeoJSON data
            
      //       this.geojsonData.features.forEach((feature: any) => {
     
      //         // Check if the circle intersects with the feature
      //        const doesIntersect = turf.booleanOverlap(turf.simplify(feature),turf.simplify(turfshape));
      //         //console.log('doesIntersect' ,doesIntersect)
          
      //         // //console.log('doesIntersect2', doesIntersect2);
              
          
      //         if (doesIntersect) {
      //           intersectingRegions.push(feature.properties.iso_a2_eh); // Adjust this based on your GeoJSON structure
      //          }else{
      //          const doesIntersect2 = turf.booleanPointInPolygon(
      //            turf.point([elt.center.lng, elt.center.lat]), // Create a Turf.js Point from circle center
      //            feature // Assuming the GeoJSON feature is a Polygon
      //          );
      //           if (doesIntersect2) {
      //             intersectingRegions.push(feature.properties.iso_a2_eh); // Adjust this based on your GeoJSON structure
      //           }
      //         }
     
     
     
             
      //       });
        
      //       console.log('zz circle>>>>',intersectingRegions);
    
    
      //       ///
      //       let  C_countryCodes:any
          
     
     
      //   await this.datacrowdService.getcountry(intersectingRegions).then((ress:any)=>{
      //    console.log('getcountry>>>>',ress);
      //    // C_subregion=ress[0];
      //    // C_region=ress[1];
      //    // C_Country=ress[2];
      //    C_countryCodes=ress;
       
      //  }) 
     
    
      //  elt.countrycodes=this.convertCountryCode(C_countryCodes);
      //     }
      //   };
        
    
        // if(this.reportType =="3"){
        //   if (!this.Coord || this.Coord.length < 2) {
    
        //     const dialogConfig = new MatDialogConfig();
        //     dialogConfig.data = {
        //       content: 'Select more than one Area',
        //     };
    
        //     this.dialog.open(ContentmodalComponent, dialogConfig);
    
        //     localStorage.clear();
        //   } else {
        //     let shouldExecuteTest = true; // Flag variable
    
        //     await this.Coord.forEach((obj : any) => {
        //       //console.log('obj>>>>>>>>', obj);
        //       if (
        //         obj.selectedStartDate === '' ||
        //         obj.selectedEndDate === '' ||
        //         typeof obj.selectedStartDate === 'undefined' ||
        //         typeof obj.selectedEndDate === 'undefined'
        //       ) {
        //         const dialogConfig = new MatDialogConfig();
        //         dialogConfig.data = {
        //           content: 'Date each Shape!!!',
        //         };
    
        //         this.dialog.open(ContentmodalComponent, dialogConfig);
        //         shouldExecuteTest = false; // Set the flag to false if any object meets the condition
        //       }
        //     });
    
        //     if (shouldExecuteTest) {
        //       this.datajson = await this.getSimulationData(queryjson);
        //       console.log('datajson>', this.datajson);        }
    
        //   }
        // }
        // else{
          this.datajson = await this.getSimulationData(queryjson);
          console.log('datajson>', this.datajson);
        // }
        
    //////////display clusters
    //return this.datajson;
    //     if (this.datajson !== null) {
    //       //console.log("this.datajson.markerPositions<<<>>>>>", this.datajson.markerPositions.length);
    //       this.marker = L.markerClusterGroup({
    //         spiderfyOnMaxZoom: false,
    //         animate: true,
    //         singleMarkerMode: true,
    //       });
    //       this.markerLoop = L.markerClusterGroup({
    //         spiderfyOnMaxZoom: false,
    //         animate: true,
    //         singleMarkerMode: true,
    //       });
    //       let lastMarkerLat:any;
    //       let lastMarkerLng:any;
    
    //       for (var j = 0; j < 1; j++) {
    //         for (var i = 0; i < this.datajson; i++) {
    //           this.markers = L.marker([
    //             Number(this.datajson[i].location_latitude),
    //             Number(this.datajson[i].location_longitude)
             
    //           ]);
    //           this.markers.off("click");
    //           this.markers.on("mousedown", (e: any) => {
    //             if (e.originalEvent.buttons == 2) {
    //               e.target.openPopup();
    
    //             }
    //             if (e.originalEvent.buttons == 1) {
    //               //  alert(1);
    //             }
    //           });
    //           this.markersArray.push(this.markers)
              
    //   lastMarkerLat = this.datajson[i][4];
    //   lastMarkerLng = this.datajson[i][3];
    //         }
    //       }
     
    
    //       //       markersBatch.push(marker);
    //       //     }
    
    //       //     // Apply event listeners to the batch of markers
    //       //     markersBatch.forEach(marker => {
    //       //       marker.off("click");
    //       //       marker.on("mousedown", (e: any) => {
    //       //         if (e.originalEvent.buttons == 2) {
    //       //           e.target.openPopup();
    //       //         }
    //       //         if (e.originalEvent.buttons == 1) {
    //       //           // alert(1);
    //       //         }
    //       //       });
    
    //       //       this.markersArray.push(marker);
    //       //     });
    
    //       //     // Clear markersBatch to free up memory
    //       //     markersBatch.length = 0;
    //       //   }
    //       // }
    //       // // End the timer and log the elapsed time
    //       // //console.timeEnd('loopTime');
    
    //       //     //  this.marker.openPopup(
    //       //     //  html11
    //       //     //  );
    
    
    
    //       this.rowData = [];
    //       this.datajson.forEach((element: any, key: any) => {
    //         this.myMarker = this.binddata(
    //           element[4],
    //           element[3],
    //           element[1],
    //           element[0],
    //           element[2],
    //           element[5],
    //           ""
    //         );
    
    //         this.myMarker.lat = element[4];
    //         this.myMarker.lng = element[3]
    //         this.myMarker.timestamp = element[1]
    //         this.myMarker.tel = element[0];
    //         this.myMarker.name = element[2];
    //         this.marker.addLayer(this.myMarker);
    //         this.markerLoop.addLayer(this.myMarker);
    //         this.myMarker.off("click");
    //         this.myMarker.on("mousedown", async (e: any) => {
    //           if (e.originalEvent.buttons == 2) {
    //             //console.log("markerChildrensssssss", e.target)
    //             this.rowData = [];
    //             var jsonaggrid = {
    //               Device_id: e.target.tel,
    //               Tel: e.target.name,
    //               Date: e.target.timestamp,
    //               Hits: "1",
    //               Coord: e.target.lat + ',' + e.target.lng,
    //               //Lat:e.target.lat
    //             };
    //             this.rowData.push(jsonaggrid);
    
    
    //             const componentfactory =
    //               this.componentFactoryResolver.resolveComponentFactory(
    //                 VAgGridComponent
    //               );
    //             const componentref =
    //               this.viewContainerRef.createComponent(componentfactory);
    //             componentref.instance.rowData = this.rowData;
    //             componentref.instance.columnDefs = this.columnDefs;
    //             componentref.instance.headerHeight = 0;
    //             // componentref.instance.selectdevices = true;
    //             componentref.instance.Title = "Here On";
    //             componentref.instance.distinct = true;
    //             componentref.changeDetectorRef.detectChanges();
    //             componentref.instance.Grid2Type = 'btn-54';
    //             componentref.instance.GridID = 'GeoGrid1';
    
    //             const html2 = componentref.location.nativeElement;
    //             await html2;
    
    //             // $('#agGrid').css('height','10px');
    //             $('.ag-theme-balham').css('height', '130px');
    
    
    //             // /  e.target.openPopup(html2, e.target._latlng);
    //             this.map.openPopup(html2, e.target._latlng);
    
    
    //           } else if (e.originalEvent.buttons == 1) {
    
    //           }
    
    //         });
    //       });
    
    //       const componentfactory =
    //         this.componentFactoryResolver.resolveComponentFactory(VAgGridComponent);
    //       const componentref =
    //         this.viewContainerRef.createComponent(componentfactory);
    //       const html1 = (componentref.location.nativeElement.style.display = "none");
    //       componentref.instance.columnDefs = this.columnDefs;
    //       componentref.changeDetectorRef.detectChanges();
    //       this.marker.off("click");
    //       this.marker.on("clustermousedown", async (e: any) => {
    //         if (e.originalEvent.buttons == 2) {
    
    //           var markerChildrens = e.layer.getAllChildMarkers();
    
    
    
    
    
    //           this.rowData = [];
    
    //           for (var j = 0; j < markerChildrens.length; j++) {
    //             var jsonaggrid = {
    //               Device_id: markerChildrens[j].tel,
    //               Tel: markerChildrens[j].name,
    //               Date: markerChildrens[j].timestamp,
    //               Hits: "1",
    //               Coord: markerChildrens[j].lat + ',' + markerChildrens[j].lng,
    //               // Lat:markerChildrens[j].lat
    //             };
    //             this.rowData.push(jsonaggrid);
    //           }
    
    //           //console.log("markerChildrens>>>>>", markerChildrens);
    
    //           const componentfactory =
    //             this.componentFactoryResolver.resolveComponentFactory(
    //               VAgGridComponent
    //             );
    //           const componentref =
    //             this.viewContainerRef.createComponent(componentfactory);
    //           componentref.instance.rowData = this.rowData;
    //           componentref.instance.columnDefs = this.columnDefs;
    //           componentref.instance.headerHeight = 0;
    //           // componentref.instance.selectdevices = true;
    //           componentref.instance.Title = "Here On";
    //           componentref.instance.distinct = true;
    //           componentref.changeDetectorRef.detectChanges();
    //           componentref.instance.pagination = false;
    //           componentref.instance.rowGrouping = true;
    //           componentref.instance.contextmenu = false;
    //           componentref.instance.Grid2Type = 'btn-54';
    //           componentref.instance.GridID = 'GeoGrid1';
    //           const html1 = componentref.location.nativeElement;
        
    //           await html1;
    //           //console.log("markerChildrens.length>>>>>>", markerChildrens.length)
    //           if (markerChildrens.length < 3) {
    //             // $('#agGrid').css('height','10px');
    //             $('.ag-theme-balham').css('height', '130px');
    
    //           } else {
    //             $('.ag-theme-balham').css('height', ' 250px ');
    
    //           }
    
    
    //           this.map.openPopup(html1, e.layer.getLatLng());
    
    //           // $(".modal-content").css("width","650px");
    //           // $(".modal-content").css("right","200px");
    //           // $(".modal-content").css("padding","10px");
    //           // $(".modal-content").css("top","85px");
    //           // $(".modal-content").draggable({
    //           //   axis: "both",
    //           //   cursor: "move"
    //           // });
    //           //  this.modalRef =this.modalService.open(this.popupContent1);
    
    //         }
    //         if (e.originalEvent.buttons == 1) {
    //           // alert(4);
    
    //         }
    
    //         //open popup;
    //       });
    
    //       this.map.addLayer(this.marker);
    //       //  this.map.setView([lastMarkerLat, lastMarkerLng],12);
          
    //       this.magnifiedMap.addLayer(this.markerLoop);
    //       this.layerGroup.addLayer(this.marker);
       
    // }
  if(this.datajson !== null){
    this.displayClusters(this.datajson);
    this.clustersZoom(this.datajson);
  }
  
  }
    
    if(this.senarioFlag==true){
      this.senariocount++;
    
    //this.isFormVisible = false;
    let obj22:any={
      senarioParentName:this.senarioParentName,
      simulationid:this.simulationid,
      Action:"addnewMenu",
      senariocount:this.senariocount,
      senarioFlag:this.senarioFlag,
      reportType:this.reportType
    }
      
    // this.senarioIdOutput.emit(obj22);
        this.navbarSimulId=obj22;
    
    
        }
        let obj:any={"table_id": this.simulationid }
    
        await this.datacrowdService.SaveSimul(obj);
     } 


     
     async bars(){
      await this.datacrowdService.getNextActionMenuList().then((response:any)=>{
      console.log("response===========",response);
      this.nextActionMenuList=response;
      
      });
        this.showTextMenu = !this.showTextMenu;
        this.ShowHeader=false;
      }
      
      CatchHeaderParam(param:any){
        console.log("param",param);
        this.Devices=param.Device;
        this.reportType=param.TYPE;
        this.selectedType=param.TYPE;
        this.ImsiID=param.IMSI_ID;
        this.DateTimeFrom=this.convertDate(param.dateTimeFrom);
        this.DateTimeTo=this.convertDate(param.dateTimeTo);
        console.log("this.dateTimeFrom-----",this.dateTimeFrom);
        console.log("this.DateTimeTo-----",this.dateTimeTo);

            }
            convertDate(dateString:any) {
              // Split the date and time parts
              let [datePart, timePart] = dateString.split('T');
              // Split the date part into year, month, and day
              let [year, month, day] = datePart.split('-');
              console.log("111111111111111----",[year, month, day]);
              timePart = timePart.replace('Z', '');
              // Format the date as MM/DD/YYYY
              let formattedDate = `${month}/${day}/${year} ${timePart}`;
            console.log("formattedDate----",formattedDate);
              return formattedDate;
            }

            convertArray(input: any[]): { id: number, name: string }[] {
              return input.map(item => ({
                id: item[0],
                name: item[1]
              }));
            }

            convertDate2(dateString: any) {
              let [datePart, timePart] = dateString.split('T');
              let [year, month, day] = datePart.split('-');
              timePart = timePart.replace('Z', '');
              let [hour, minute, second] = timePart.split(':');
              let formattedDate = `${month}/${day}/${year} ${hour}:${minute}`;
              console.log("formattedDate----", formattedDate);
              return formattedDate;
            }

            
  async getSimulationData(obj: any) {
    //  this.datacrowdService.getSimulationData(obj)
    //   .then(response => {
    //     console.log('getSimulationData>>>>>> 0', response);
    //     this.simulationData = response;
    //   })
    //   .catch(error => {
    //     console.error('Error fetching simulation data', error);
    //   });



    await this.datacrowdService.getSimulationData(obj).then((data:any) => {
        this.records = data ;

        let parsedJson = {};
        console.log('array buffer response ---------------',this.records);
         
       
 
      }); 
      let e=  this.byteToJson(this.records);
       console.log('eeeeeeeee>>>>>> 0', e);
      return e;
      
    }

    byteToJson(records:any){
      
      console.log("tttttttttttttttt ------------");
      let contentType = records.headers.get('content-type');
      let byteArray = new Uint8Array(records.body);
 
                console.log('byteArray>>>>>> 0', byteArray);
                  // Use TextDecoder to convert byteArray to string
  const decoder = new TextDecoder('utf-8');
  let bytesString = decoder.decode(byteArray);

  console.log('Bytes to string: ', bytesString);
                // let bytesString = String.fromCharCode(...byteArray)

                // console.log('Bytes to string: ', bytesString);
                let stringtojson = JSON.parse(bytesString);
                console.log('JSON:111111111 ', stringtojson );
                return stringtojson;
    }

}

