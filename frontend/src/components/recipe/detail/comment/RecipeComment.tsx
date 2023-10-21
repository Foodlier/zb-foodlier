import React, { useState, useEffect } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/FetchCall'
import CommonButton from '../../../ui/Button'
import ModalWithTwoButton from '../../../ui/ModalWithTwoButton'

interface CommentItem {
  id: number
  message: string
  nickname: string
  createdAt: string
  profileImageUrl: string
}

const RecipeComment = () => {
  // 댓글의 memberid 와 현재 로그인한 memberid 값을 비교하면됨
  const memberId = 2 // 여기다가 현재 로그인한 memberid를 넣고
  // recipeId 변수 선언 - useParams 동적 라우팅은 나중에 연결할것
  const [recipeId] = useState(1)
  // 댓글 수정 시 어떤 댓글을 수정할 것인지 추적
  const [commentId, setCommentId] = useState<number | null>(null)
  // 변수에 활용한 CommentItem 인터페이스 - 같은 객체 구조를 가져야함
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  // 수정 버튼 활성화를 위한 isEditing useState 변수, setIsEditing useState 함수
  const [isEditing, setIsEditing] = useState(false)
  // pageIdx useState 변수
  const [pageIdx, setPageIdx] = useState(0)
  // useRef 함수는 current 속성을 가지고 있는 객체 반환(초기값 5 current 속성에 할당)
  const pageSize = 5
  const [hasNextCommentPage, setHasNextCommentPage] = useState(true)
  // message value 값인 commentValue useState 변수
  const [commentValue, setCommentValue] = useState('')
  // 댓글을 수정할 때 입력한 내용을 editValue 상태 변수에 저장하려면 setEditValue를 사용하여 값을 업데이트
  const [editValue, setEditValue] = useState('')
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)
  const [willModifyCommentId, setWillModifyCommentId] = useState(0)

  const getComment = async () => {
    // recipeId - 1 / pageIdx - 0 / pageSize - 5 (추후 recipeId는 useParam 쓰기)
    const res = await axiosInstance.get(
      `/comment/${recipeId}/${pageIdx}/${pageSize}`
    )

    if (res.status === 200) {
      const commentData = res.data.content
      console.log(commentData)

      setHasNextCommentPage(res.data.hasNextPage)
      if (pageIdx === 0) {
        // 첫 번째 페이지의 경우, 댓글 목록을 바로 설정
        setCommentList(commentData)

        console.log('끝인지 확인하는거 ::: ', res.data.hasNextPage)
      } else {
        // 이후 페이지에서는 이전 댓글 목록과 새로운 댓글 목록을 합친다.
        setCommentList(prevCommentList => [...prevCommentList, ...commentData])
      }
    }
  }

  const onSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    try {
      const res = await axiosInstance.post(`/comment/${recipeId}?message=${commentValue}`)
      if (res.status === 200) {
        console.log('res.config.data', res.config.data)
        getComment()
        // 요청이 성공했을 경우, commentValue 상태 변수를 비운다.
        setCommentValue('')
      }
    } catch (error) {
      console.error('새 댓글 등록 중 오류 발생:', error)
    }
  }
  
  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    // 해당 변화에 따라 commentValue 상태를 업데이트하는 setCommentValue 함수
    setCommentValue(event.currentTarget.value)
  }

  // 댓글 더보기
  const handleLoadMoreComments = () => {
    setPageIdx(prevPageIdx => prevPageIdx + 1)
    getComment()
  }

  // 수정 버튼 누르면
  const handleEditComment = (
    commentIdParams: number, // 수정할 댓글의 고유 식별자
    modifiedMessageParams: string // 수정할 메시지 내용인 문자열
  ) => {
    setIsEditing(true) // 댓글 수정 모드에 들어갔음
    setEditValue(modifiedMessageParams) // 수정할 댓글의 내용을 저장
    setWillModifyCommentId(commentIdParams) // 나중에 댓글을 실제로 수정할 때 어떤 댓글을 수정할 것인지 나타내는데 사용
    setCommentId(commentIdParams) // commentId 상태를 commentIdParams 값으로 설정
    console.log('수정버튼누르면', commentIdParams)
  }

  // 댓글 수정 취소
  const handleCancelEdit = () => {
    setIsEditing(false)
    setEditValue('')
  }

  // 삭제 버튼 누름
  const handleDeleteComment = (commentIdParams: number) => {
    setIsDeleteModalOpen(true)
    setCommentId(commentIdParams)
    // console.log('삭제버튼 누름', commentIdParams)
  }

  // 삭제 모달 확인 버튼
  const handleConfirmDelete = async () => {
    // console.log('확인중입니다', commentId)

    const res = await axiosInstance.delete(`/comment/${recipeId}/${commentId}`)
    try {
      if (res.status === 200) {
        console.log('삭제성공?!')
      }
    } catch (error) {
      console.error('삭제 안되서 에러남', error)
    } finally {
      setIsDeleteModalOpen(false)
    }
  }

  // 수정 확인 버튼 클릭 시
  const handleEditConfirmClick = async () => {
    // console.log('commentId !! : ', commentId, editValue)
    try {
      const body = {
        modifiedMessage: editValue,
      }
      const res = await axiosInstance.put(`/comment/${commentId}`, body)
      if (res.status === 200) {
        // console.log('댓글 수정 성공', editValue)

        setIsEditing(false)
        setEditValue('')
      }
    } catch (error) {
      console.error('댓글 업데이트 실패', error)
    }
  }

  // 마운트 시 렌더링 1번만 실행
  useEffect(() => {
    // 댓글 가져오는(get) 함수 실행
    getComment()
  }, [])

  // commentList가 있고 마운트 시에 실행
  useEffect(() => {
    // console.log('commentList 콘솔', commentList)
  }, [commentList])

  return (
    <S.CommentContainer>
      <S.CommentTit>댓글</S.CommentTit>
      <S.CommentForm>
        <S.CommentInput
          type="text"
          placeholder="댓글을 입력하세요"
          onChange={handleClick}
          value={commentValue}
        />
        <S.CommentSubmit onClick={onSubmit}>등록</S.CommentSubmit>
      </S.CommentForm>
      <S.CommentList>
        {commentList.map(commentItem => (
          <S.CommentItemWrapper key={commentItem.id}>
            <S.CommentHeader>
              <S.CommentUserInfo>
                <S.UserImg
                  src={commentItem.profileImageUrl}
                  alt={commentItem.nickname}
                />
                <S.UserNickname>{commentItem.nickname}</S.UserNickname>
              </S.CommentUserInfo>
              <S.CommentDate>
                {commentItem.createdAt.slice(0, 10)}
              </S.CommentDate>
            </S.CommentHeader>
            {isEditing && commentItem.id === willModifyCommentId ? (
              <S.CommentEdit>
                <S.CommentEditInput
                  value={editValue}
                  onChange={e => setEditValue(e.target.value)}
                />
                <S.CommentButtonWrap>
                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={handleEditConfirmClick}
                  >
                    확인
                  </CommonButton>
                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={handleCancelEdit}
                  >
                    취소
                  </CommonButton>
                </S.CommentButtonWrap>
              </S.CommentEdit>
            ) : (
              <>
                <button
                  type="button"
                  onClick={() => {
                    console.log(commentItem.id, commentItem.message)
                  }}
                >
                  test
                </button>
                <S.CommentContent>{commentItem.message}</S.CommentContent>

                {/* line17의 memberId와 해당 commentId와 비교  */}
                {memberId === 2 ? (
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() =>
                        handleEditComment(commentItem.id, commentItem.message)
                      }
                    >
                      수정
                    </CommonButton>

                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() => handleDeleteComment(commentItem.id)}
                    >
                      삭제
                    </CommonButton>
                  </S.CommentButtonWrap>
                ) : (
                  <S.CommentButtonWrap />
                )}
              </>
            )}
          </S.CommentItemWrapper>
        ))}
      </S.CommentList>
      <S.MoreButtonBox>
        <CommonButton
          onClick={handleLoadMoreComments}
          color={hasNextCommentPage ? 'main' : 'white'}
          border={hasNextCommentPage ? 'border' : 'borderNone'}
        >
          {hasNextCommentPage ? '댓글 더보기' : ''}
        </CommonButton>
      </S.MoreButtonBox>
      {/* 삭제 Modal */}
      {isDeleteModalOpen && (
        <ModalWithTwoButton
          content="정말 삭제하시겠어요?"
          subContent="샥제 버튼 선택 시, 댓글은 삭제되며 복구되지 않습니다."
          setIsModalFalse={() => setIsDeleteModalOpen(false)}
          modalEvent={handleConfirmDelete}
        />
      )}
    </S.CommentContainer>
  )
}
export default RecipeComment