import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import useIcon from '../../../../hooks/useIcon'
import RecipeReviewItem from './RecipeReviewItem'
import CommonButton from '../../../ui/Button'
import * as S from '../../../../styles/recipe/detail/review/RecipeReviewList.styled'
import { palette } from '../../../../constants/Styles'
import axiosInstance from '../../../../utils/FetchCall'

interface ReviewItem {
  recipeId: number
  recipeReviewId: number
  nickname: string
  profileUrl: string
  content: string
  star: number
  cookUrl: string
  createdAt: string
}

function RecipeReviewList() {
  // const initialReviews = [
  //   {
  //     recipeReviewId: '1',
  //     nickname: '사용자1',
  //     content:
  //       'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galle',
  //     star: 5,
  //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //     cookUrl: 'https://source.unsplash.com/random/300x300/?food',
  //     createdAt: '2023-10-07',
  //   },
  //   {
  //     recipeReviewId: '2',
  //     nickname: '사용자2',
  //     content: '너무 맛있어요!',
  //     star: 4,
  //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //     cookUrl: 'https://source.unsplash.com/random/300x300/?food',
  //     createdAt: '2023-10-08',
  //   },
  //   {
  //     recipeReviewId: '3',
  //     nickname: '사용자1',
  //     content:
  //       'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galle',
  //     star: 5,
  //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //     cookUrl: 'https://source.unsplash.com/random/300x300/?food',
  //     createdAt: '2023-10-07',
  //   },
  //   {
  //     recipeReviewId: '4',
  //     nickname: '사용자2',
  //     content: '너무 맛있어요!',
  //     star: 3,
  //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //     cookUrl: 'https://source.unsplash.com/random/300x300/?food',
  //     createdAt: '2023-10-08',
  //   },
  //   {
  //     recipeReviewId: '5',
  //     nickname: '사용자1',
  //     content:
  //       'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galle',
  //     star: 2,
  //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //     cookUrl: 'https://source.unsplash.com/random/300x300/?food',
  //     createdAt: '2023-10-07',
  //   },
  //   {
  //     recipeReviewId: '6',
  //     nickname: '사용자2',
  //     content: '너무 맛있어요!',
  //     star: 1,
  //     profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  //     cookUrl: 'https://source.unsplash.com/random/300x300/?food',
  //     createdAt: '2023-10-08',
  //   },
  // ]
  const [reviewList, setReviewList] = useState<ReviewItem[]>([])
  // const [myReview, setMyReview] = useState<ReviewItem>()
  const [reviews, setReviews] = useState([])
  const [visibleReviews, setVisibleReviews] = useState(5)
  const { IcAddLight } = useIcon()

  const fetchMyReviewData = async () => {
    const res = await axiosInstance.get(`/review/recipe/me/1
    `)

    if (res.status === 200) {
      const myReviewData = res.data
      console.log('내꺼 리뷰 확인 :', myReviewData)

      // setMyReview(myReviewData)

      console.log('내꺼 데이터 넣고나서 :: ', myReviewData)
    } else {
      console.log('내꺼리뷰 가져오는거실패 ')
    }
  }
  const fetchReviewData = async () => {
    const res = await axiosInstance.get(`/review/recipe/0/5/1`)

    if (res.status === 200) {
      const reviewData = res.data.content
      console.log('리뷰 확인 :', reviewData)

      setReviewList(reviewData)

      console.log('데이터 넣고나서 :: ', reviewList)

      // 이후 예외처리 해야함
      // if (pageIdx === 0) {
      //   // 첫 번째 페이지의 경우, 댓글 목록을 바로 설정
      //   setCommentList(commentData)
      // } else {
      //   // 이후 페이지에서는 이전 댓글 목록과 새로운 댓글 목록을 합친다.
      //   setCommentList(prevCommentList => [...prevCommentList, ...commentData])
      // }

      // if (pageIdx >= res.data.totalPages - 1) {
      //   endOfList.current = true
      // } else {
      //   endOfList.current = false
      // }
    }
  }
  useEffect(() => {
    // 리뷰 가져오는(get) 함수 실행
    fetchMyReviewData()
    fetchReviewData()
  }, [])

  const handleShowMore = () => {
    console.log(setReviews)
    setVisibleReviews(visibleReviews + 5)
  }

  return (
    <S.ReviewContainer>
      <S.ReviewtitBox>
        <S.Reviewtit>후기</S.Reviewtit>
        <Link to="/recipe/detail/write-review">
          <CommonButton color="main">
            <IcAddLight size={2} color={palette.white} />
            후기 작성하기
          </CommonButton>
        </Link>
      </S.ReviewtitBox>

      {/* <RecipeReviewItem key={myReview?.recipeReviewId} review = {myReview} /> */}
      <S.ReviewUlBox>
        {reviewList.slice(0, visibleReviews).map(review => (
          <RecipeReviewItem key={review.recipeReviewId} review={review} />
        ))}
      </S.ReviewUlBox>

      <S.MoreButtonBox>
        {reviews.length > 5 && visibleReviews < reviews.length && (
          <CommonButton onClick={handleShowMore} color="main" border="border">
            후기 더보기
          </CommonButton>
        )}
      </S.MoreButtonBox>
    </S.ReviewContainer>
  )
}

export default RecipeReviewList
