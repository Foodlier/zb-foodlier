export interface Slide {
  recipeId: string | number
  title: string
  introduce: string
  imagePath: string
  likeCount: number
  isLike: boolean
}

export const slides: Slide[] = [
  {
    recipeId: 1,
    title: '마크정식',
    introduce:
      '커뮤니티에서 핫했던, 이제는 편의점 조합이라면 제일 먼저 생각나는 마크정식의 원조 레시피입니다',
    imagePath: '/images/contents/food_img_01.jpg',
    isLike: true,
    likeCount: 100,
  },
  {
    recipeId: 2,
    title: '포테딕트',
    introduce:
      '이 레시피라면 감자 한박스 해결 가능이지!! 오늘 저녁 포테딕트 어떠세요?',
    imagePath: '/images/contents/food_img_02.jpg',
    isLike: false,
    likeCount: 100,
  },
  {
    recipeId: 3,
    title: '유부초밥',
    introduce:
      '달달하면서 짭짤한 베스트 도시락 메뉴! 어떤 재료를 추가해도 맛있는 유부초밥! 더 맛있게 즐길 수 있는 특별한 레시피를 소개할게요~',
    imagePath: '/images/contents/food_img_03.jpg',
    isLike: false,
    likeCount: 100,
  },
  {
    recipeId: 4,
    title: '훈제오리냉채',
    introduce:
      '여름이라 조금 더 자극적이면서 시원한 걸 찾게 되는 것 같아요 코 찡~한 겨자소스에 상큼한 채소들을 곁들인 냉채도 여기에 속하는데요 여름엔 아예 겨자소스를 잔뜩 만들어 놓곤 냉장고 속 채소들 대충 준비해 샐러드로도 즐기고 있어요',
    imagePath: '/images/contents/food_img_04.jpg',
    isLike: false,
    likeCount: 110,
  },
  {
    recipeId: 5,
    title: '차돌박이찜',
    introduce:
      '손님상차림으로도 좋은 차돌박이찜 입니다. 만들기도 너무쉽고 모양도 예뻐서 추천하는 메뉴에요',
    imagePath: '/images/contents/food_img_05.jpg',
    isLike: false,
    likeCount: 150,
  },
  {
    recipeId: 6,
    title: '참치마요덮밥',
    introduce:
      '냉장고를 열었더니 참치가 눈에 딱 들어와서 무엇을 만들까 생각했는데, 제가 제일 좋아하는 메뉴 중 하나. 참치마요덮밥을 만들어봤어요. 학창시절에 친구들과 간단하게 한끼 해결하기 위해 자주 먹었던 메뉴이기도 해요. 마요덮밥은 참치마요, 치킨마요, 스팸마요 등 다양한 메뉴들이 있지요~ 마요네즈 들어간 덮밥요리들은 다 인기가 많기도 하고 맛있더라구요^^ 오늘은 참치마요덮밥 만들어볼까요?',
    imagePath: '/images/contents/food_img_06.jpg',
    isLike: false,
    likeCount: 100,
  },
]
