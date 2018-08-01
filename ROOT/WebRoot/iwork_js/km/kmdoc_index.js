    Ext.onReady(function(){
    
        // NOTE: This is an example showing simple state management. During development,
        // it is generally best to disable state management as dynamically-generated ids
        // can change across page loads, leading to unpredictable results.  The developer
        // should ensure that stable state ids are set for stateful components in real apps.
        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
        var KMView = new Ext.Viewport({
            layout: 'border',
            items: [{
                region: 'east',
                title: '文档相关',
                collapsible: true,
                split: true,
                width: 225, // give east and west regions a width
                minSize: 175,
                maxSize: 400,
                margins: '0 5 0 0',
                layout: 'fit', // specify layout manager for items
                items:            // this TabPanel is wrapped by another Panel so the title will be applied
                new Ext.TabPanel({
                    border: false, // already wrapped so don't add another border
                    activeTab: 0, // second tab initially active
                    tabPosition: 'bottom',
                    items: [{
                        html:'<iframe name=KMBASEINFO id=KMBASEINFO  frameborder=0 width=100% scrolling="no" height=100% src="km_doc_load.action"></iframe>',
                        title: '基本信息',
                        autoScroll: true 
                    },{
                        html: '<p>A TabPanel component can be a region.</p>',
                        title: '历史版本',
                        autoScroll: true
                    },{
                        html: '<p>A TabPanel component can be a region.</p>',
                        title: '文档相关',
                        autoScroll: true
                    }]
                })
            }, {
                region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                title: '知识文档',
                split: true,
                width: 200,
                minSize: 175,
                maxSize: 400,
                collapsible: true,
                margins: '0 0 0 5',
                layout: {
                    type: 'accordion',
                    animate: true
                },
                items: [{
                    contentEl: 'west',
                    title: '目录导航',
                    border: false,
                    html:'<iframe name=KM_ENTERPRISE_TREE_IFRAME src=km_enterprise_tree.action frameborder=0 width=100% height=100% src=../aws_html/load.htm></iframe>',
                    iconCls: 'nav' // see the HEAD section for style used
                }, {
                    title: '共享文档',
                    html: '<p>Some settings in here.</p>',
                    border: false,
                    iconCls: 'settings'
                }, {
                    title: '收藏夹',
                    html: '<p>Some settings in here.</p>',
                    border: false,
                    iconCls: 'settings'
                }, {
                    title: '任务',
                    html: '<p>Some settings in here.</p>',
                    border: false,
                    iconCls: 'settings'
                }]
            },
            // in this instance the TabPanel is not wrapped by another panel
            // since no title is needed, this Panel is added directly
            // as a Container
           kmTabs=  new Ext.TabPanel({
                region: 'center', // a center region is ALWAYS required for border layout
                deferredRender: false,
                activeTab: 0,     // first tab initially active
                items: [{
                    contentEl: 'center1',
                    title: '文档列表',
                    closable: false,
                    autoScroll: true
                }, {
                    contentEl: 'center2',
                    title: '文档检索',
                    closable: true,
                    autoScroll: true
                }]
            })]
        });
        // get a reference to the HTML element with id "hideit" and add a click listener to it 
        Ext.get("hideit").on('click', function(){
            // get a reference to the Panel that was created with id = 'west-panel' 
            var w = Ext.getCmp('west-panel');
            // expand or collapse that Panel based on its collapsed property state
            w.collapsed ? w.expand() : w.collapse();
        });
    });