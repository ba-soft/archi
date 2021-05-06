function setRootPanelHeight() {
	$('.root-panel-body').css('height', $('.root-panel').outerHeight() - $('.root-panel-heading').outerHeight());
}

function strcmp(a, b){
	var aText = $(a).text().trim().toLowerCase();
	var bText = $(b).text().trim().toLowerCase();
	if (aText.toString() < bText.toString()) return -1;
  if (aText.toString() > bText.toString()) return 1;
  return 0;
}

$(document).ready(function() {
	// Set jQuery UI Layout panes
  $('body').layout({
    minSize: 45,
    maskContents: true,
    north: {
      size: 45,
      spacing_open: 8,
      closable: false,
      resizable: false
    },
    west: {
			size: 350,
			spacing_open: 8
		},
    west__childOptions: {
      maskContents: true,
      south: {
	      minSize: 50,
				size: 250,
				spacing_open: 8
			},
			center: {
				minSize: 50,
				onresize: "setRootPanelHeight"
			}
    }
  });
	
	// Set heigh of panels the first time
	setRootPanelHeight();
	
	// Setup modeltree
	$('.tree li:has(ul)').addClass('parent_li').find(' > ul > li').hide();

	// Add show/hide function on modeltree
	$('.tree li.parent_li > span').on('click', function (e) {
		var children = $(this).parent('li.parent_li').find(' > ul > li');
		if (children.is(":visible")) {
			children.hide('fast');
			$(this).find(' > i').addClass('glyphicon-triangle-right').removeClass('glyphicon-triangle-bottom');
		} else {
			// START SORT
			$(this).parent('li.parent_li').find(' > ul').each(function(index){
				$(this).children('li.tree-folder').sort(strcmp).appendTo($(this));
				$(this).children('li.tree-element').sort(strcmp).appendTo($(this));
			});
			// END SORT
			children.show('fast');
			$(this).find(' > i').addClass('glyphicon-triangle-bottom').removeClass('glyphicon-triangle-right');
		}
		e.stopPropagation();
	});
	
	
	// *** DEEPLINKS ***

	// Register a new onClick function
	let $viewLinks = $("a[href][target='view']");
	$viewLinks.on('click', function (event) {
		const id = getIdFromHref(event.currentTarget.href);
		setLocationForView(id);
		openViewFromLocation(false);
		event.stopPropagation();
		return false;
	});

	// Load initial view id on page load
	openViewFromLocation(true); 

	function openViewFromLocation(expandModelTree) {
		// Find matching view in model tree...
		const targetId = getIdFromLocation();
		const matchingLinks = $viewLinks.filter(function (index, element) {
			return getIdFromHref(element.href) === targetId;
		});
		const link = matchingLinks[0];

		if (link) {
			// View found in model tree. Loading it in frame
			const $link = $(link);
			$("iframe[name='view']").attr('src', $link.attr('href'));

			if (expandModelTree) {
				let spans = [];
				let $parentListItem = $link.parent().parent().parent();
				while ($parentListItem[0].tagName === 'LI') {
					spans.push($parentListItem.children().first());
					$parentListItem = $parentListItem.parent().parent();
				}
				while (spans.length) {
					spans.pop().click();
				}
			}
		}
	}

	function setLocationForView(id) {
		const url = new URL(window.location);
		url.searchParams.set('view', id);
		window.history.pushState({}, '', url);
	}

	function getIdFromHref(href) {
		return href.split("/").pop().slice(0, -5);
	}

	function getIdFromLocation() {
		const url = new URL(window.location);
		return url.searchParams.get('view');
	}

	$(window).on('message', function (e) {
			const id = e.originalEvent.data.split('=').pop();
			setLocationForView(id);
			//openViewFromLocation(true); 
		});
});
